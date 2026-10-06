import { Component, DestroyRef, computed, inject, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { RouterLink } from '@angular/router';
import { DatePipe, CurrencyPipe, TitleCasePipe } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatTableModule } from '@angular/material/table';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { ApiService, Appointment, Dentist, PageResult, Patient, Procedure } from '../../core/api.service';
import { ToastService } from '../../shared/toast.service';

interface Dashboard { patients: number; dentists: number; todayAppointments: number; weekAppointments: number; pendingAppointments: number; monthRevenue: number; upcoming: Appointment[]; }
interface OdontogramEntry { id:number; patientId:number; toothNumber:string; condition:string; observation?:string; }
interface Quote { id:number; patientName:string; createdAt:string; total:number; status:string; }
interface Payment { id:number; patientName:string; amount:number; paidAt:string; method:string; status:string; description?:string; }
interface RecordData { id:number; patientId:number; patientName:string; chiefComplaint?:string; anamnesis?:string; diagnosis?:string; observations?:string; procedures?:string; medications?:string; allergies?:string; dentalHistory?:string; }

@Component({ selector:'app-workspace', standalone:true, imports:[DatePipe,CurrencyPipe,TitleCasePipe,RouterLink,ReactiveFormsModule,MatIconModule,MatButtonModule,MatFormFieldModule,MatInputModule,MatSelectModule,MatTableModule,MatProgressSpinnerModule], templateUrl:'./workspace.component.html', styleUrl:'./workspace.component.scss' })
export class WorkspaceComponent {
  private readonly api=inject(ApiService); private readonly fb=inject(FormBuilder); private readonly toast=inject(ToastService); private readonly route=inject(ActivatedRoute); private readonly destroyRef=inject(DestroyRef);
  readonly section=signal('dashboard'); readonly loading=signal(false); readonly saving=signal(false); readonly formOpen=signal(false); readonly query=signal(''); readonly selectedPatientId=signal<number|null>(null); readonly selectedResourceId=signal<number|null>(null);
  readonly agendaMode=signal<'day'|'week'>('week');
  readonly patients=signal<Patient[]>([]); readonly patientPage=signal(0); readonly patientTotal=signal(0); readonly dentists=signal<Dentist[]>([]); readonly appointments=signal<Appointment[]>([]); readonly procedures=signal<Procedure[]>([]); readonly payments=signal<Payment[]>([]); readonly quotes=signal<Quote[]>([]); readonly odontogram=signal<OdontogramEntry[]>([]); readonly record=signal<RecordData|null>(null); readonly dashboard=signal<Dashboard|null>(null);
  readonly patientForm=this.fb.nonNullable.group({name:['',[Validators.required]],cpf:['',[Validators.required]],birthDate:[''],gender:[''],phone:[''],email:[''],address:[''],notes:['']});
  readonly dentistForm=this.fb.nonNullable.group({name:['',[Validators.required]],cro:['',[Validators.required]],specialty:['',[Validators.required]],phone:[''],email:['',[Validators.required,Validators.email]],active:[true]});
  readonly appointmentForm=this.fb.nonNullable.group({patientId:[0,[Validators.min(1)]],dentistId:[0,[Validators.min(1)]],startsAt:['',[Validators.required]],endsAt:['',[Validators.required]],type:['',[Validators.required]],notes:[''],status:['AGENDADA']});
  readonly recordForm=this.fb.nonNullable.group({chiefComplaint:[''],anamnesis:[''],diagnosis:[''],observations:[''],procedures:[''],medications:[''],allergies:[''],dentalHistory:['']});
  readonly odontogramForm=this.fb.nonNullable.group({toothNumber:['',[Validators.required]],condition:['CARIE',[Validators.required]],observation:['']});
  readonly procedureForm=this.fb.nonNullable.group({name:['',[Validators.required]],description:[''],price:[0,[Validators.min(0)]],durationMinutes:[30,[Validators.min(1)]],active:[true]});
  readonly paymentForm=this.fb.nonNullable.group({patientId:[0,[Validators.min(1)]],procedureId:[0],amount:[0,[Validators.min(0.01)]],paidAt:[this.localNow() ,[Validators.required]],method:['PIX',[Validators.required]],status:['PENDENTE',[Validators.required]],description:['']});
  readonly quoteForm=this.fb.nonNullable.group({patientId:[0,[Validators.min(1)]],procedureId:[0,[Validators.min(1)]],quantity:[1,[Validators.min(1)]],discount:[0,[Validators.min(0)]],notes:['']});
  readonly title=computed(()=>({dashboard:'Visão geral',pacientes:'Pacientes',dentistas:'Dentistas',agenda:'Agenda',prontuarios:'Prontuários',odontograma:'Odontograma',procedimentos:'Procedimentos',orcamentos:'Orçamentos',financeiro:'Financeiro',configuracoes:'Configurações'}[this.section()] ?? 'Visão geral'));
  readonly subtitle=computed(()=>({dashboard:'Resumo da clínica e dos atendimentos.',pacientes:'Cadastro e acompanhamento de pacientes.',dentistas:'Equipe clínica e especialidades.',agenda:'Organize os atendimentos da clínica.',prontuarios:'Histórico clínico por paciente.',odontograma:'Condições odontológicas por dente.',procedimentos:'Catálogo de serviços odontológicos.',orcamentos:'Planos de tratamento e valores.',financeiro:'Receitas e pagamentos registrados.',configuracoes:'Preferências do sistema.'}[this.section()] ?? ''));
  readonly patientColumns=['name','cpf','phone','email','actions']; readonly dentistColumns=['name','cro','specialty','phone','active','actions']; readonly appointmentColumns=['startsAt','patientName','dentistName','type','status','actions']; readonly procedureColumns=['name','price','durationMinutes','active','actions']; readonly financialColumns=['patientName','description','paidAt','method','amount','status'];
  constructor(){ this.route.paramMap.pipe(takeUntilDestroyed(this.destroyRef)).subscribe(params=>{this.section.set(params.get('section')||'dashboard');this.formOpen.set(false);this.loadSection();}); }
  loadSection():void { this.loading.set(true); switch(this.section()){
    case 'dashboard':this.api.get<Dashboard>('dashboard').subscribe({next:data=>{this.dashboard.set(data);this.loading.set(false);},error:()=>this.failed()});break;
    case 'pacientes':this.loadPatients();this.loading.set(false);break;
    case 'dentistas':this.api.get<PageResult<Dentist>|Dentist[]>('dentistas').subscribe({next:data=>{this.dentists.set(Array.isArray(data)?data:data.content);this.loading.set(false);},error:()=>this.failed()});break;
    case 'agenda':this.loadAgenda();break;
    case 'prontuarios':case 'odontograma':this.loadPatients();this.loading.set(false);break;
    case 'procedimentos':this.loadProcedures();break;
    case 'orcamentos':this.loadPatients();this.loadProcedures();this.api.get<Quote[]>('orcamentos').subscribe({next:data=>this.quotes.set(data),error:()=>this.failed()});this.loading.set(false);break;
    case 'financeiro':this.loadPatients();this.loadProcedures();this.api.get<Payment[]>('pagamentos').subscribe({next:data=>this.payments.set(data),error:()=>this.failed()});this.loading.set(false);break;
    case 'configuracoes':this.loading.set(false);break;
    default:this.loading.set(false);
  }}
  loadPatients():void { this.api.get<PageResult<Patient>>('pacientes',{q:this.query(),page:this.patientPage(),size:10}).subscribe({next:data=>{this.patients.set(data.content);this.patientTotal.set(data.totalElements);},error:()=>this.failed()}); }
  searchPatients(value:string):void {this.query.set(value);this.patientPage.set(0);this.loadPatients();}
  changePatientPage(delta:number):void {this.patientPage.update(page=>Math.max(0,page+delta));this.loadPatients();}
  loadAgenda():void {const now=new Date();const end=new Date(now);end.setDate(end.getDate()+(this.agendaMode()==='day'?0:7));this.api.get<Appointment[]>('consultas',{from:this.localDate(now),to:this.localDate(end)}).subscribe({next:data=>{this.appointments.set(data);this.loading.set(false);},error:()=>this.failed()});this.api.get<PageResult<Dentist>|Dentist[]>('dentistas').subscribe({next:data=>this.dentists.set(Array.isArray(data)?data:data.content)});this.loadPatients();}
  setAgendaMode(mode:'day'|'week'):void {this.agendaMode.set(mode);this.loading.set(true);this.loadAgenda();}
  loadProcedures():void {this.api.get<Procedure[]>('procedimentos').subscribe({next:data=>{this.procedures.set(data);this.loading.set(false);},error:()=>this.failed()});}
  openCreate():void {this.formOpen.set(true);this.resetForms();}
  closeForm():void {this.formOpen.set(false);}
  editPatient(patient:Patient):void {this.patientForm.setValue({name:patient.name,cpf:patient.cpf,birthDate:patient.birthDate??'',gender:patient.gender??'',phone:patient.phone??'',email:patient.email??'',address:patient.address??'',notes:patient.notes??''});this.selectedPatientId.set(patient.id);this.formOpen.set(true);}
  savePatient():void {if(this.patientForm.invalid)return;this.saving.set(true);const value=this.patientForm.getRawValue();const body={...value,birthDate:value.birthDate||null};const request=this.selectedPatientId()?this.api.put(`pacientes/${this.selectedPatientId()}`,body):this.api.post('pacientes',body);request.subscribe({next:()=>{this.saving.set(false);this.formOpen.set(false);this.toast.success('Paciente salvo.');this.loadPatients();},error:()=>this.saveFailed()});}
  deactivatePatient(id:number):void {if(!confirm('Deseja inativar este paciente? O histórico será preservado.'))return;this.api.delete(`pacientes/${id}`).subscribe({next:()=>{this.toast.success('Paciente inativado.');this.loadPatients();},error:()=>this.toast.error('Não foi possível inativar o paciente.')});}
  deactivateDentist(id:number):void {if(!confirm('Deseja inativar este dentista? O histórico será preservado.'))return;this.api.delete(`dentistas/${id}`).subscribe({next:()=>{this.toast.success('Dentista inativado.');this.loadSection();},error:()=>this.toast.error('Não foi possível inativar o dentista.')});}
  editDentist(dentist:Dentist):void {this.selectedResourceId.set(dentist.id);this.dentistForm.setValue({name:dentist.name,cro:dentist.cro,specialty:dentist.specialty,phone:dentist.phone??'',email:dentist.email,active:dentist.active});this.formOpen.set(true);}
  saveDentist():void {if(this.dentistForm.invalid)return;this.saving.set(true);const request=this.selectedResourceId()?this.api.put(`dentistas/${this.selectedResourceId()}`,this.dentistForm.getRawValue()):this.api.post('dentistas',this.dentistForm.getRawValue());request.subscribe({next:()=>{this.saving.set(false);this.formOpen.set(false);this.toast.success('Dentista salvo.');this.loadSection();},error:()=>this.saveFailed()});}
  saveAppointment():void {if(this.appointmentForm.invalid)return;this.saving.set(true);this.api.post('consultas',this.appointmentForm.getRawValue()).subscribe({next:()=>{this.saving.set(false);this.formOpen.set(false);this.toast.success('Consulta agendada.');this.loadAgenda();},error:error=>{this.saving.set(false);this.toast.error(error.error?.message||'Não foi possível agendar. Verifique os horários.');}});}
  cancelAppointment(id:number):void {this.api.delete(`consultas/${id}`).subscribe({next:()=>{this.toast.success('Consulta cancelada.');this.loadAgenda();},error:()=>this.toast.error('Não foi possível cancelar.')});}
  selectPatient(id:number):void {this.selectedPatientId.set(id);if(this.section()==='prontuarios')this.loadRecord(id);else this.loadOdontogram(id);}
  loadRecord(id:number):void {this.api.get<RecordData>(`prontuarios/paciente/${id}`).subscribe({next:r=>{this.record.set(r);this.recordForm.patchValue(r);},error:error=>{this.record.set(null);this.recordForm.reset();if(error.status!==404)this.failed();}});}
  saveRecord():void {const id=this.selectedPatientId();if(!id)return;this.api.post<RecordData>('prontuarios',{patientId:id,...this.recordForm.getRawValue()}).subscribe({next:r=>{this.record.set(r);this.toast.success('Prontuário atualizado.');},error:()=>this.toast.error('Não foi possível salvar o prontuário.')});}
  loadOdontogram(id:number):void {this.api.get<OdontogramEntry[]>(`odontograma/paciente/${id}`).subscribe({next:data=>this.odontogram.set(data),error:()=>this.failed()});}
  toothNumbers=['18','17','16','15','14','13','12','11','21','22','23','24','25','26','27','28','48','47','46','45','44','43','42','41','31','32','33','34','35','36','37','38'];
  toothEntry(tooth:string):OdontogramEntry|undefined{return this.odontogram().find(entry=>entry.toothNumber===tooth);}
  chooseTooth(tooth:string):void {this.odontogramForm.controls.toothNumber.setValue(tooth);}
  saveTooth():void {const id=this.selectedPatientId();if(!id||this.odontogramForm.invalid)return;this.api.post('odontograma',{patientId:id,...this.odontogramForm.getRawValue()}).subscribe({next:()=>{this.toast.success('Dente atualizado.');this.loadOdontogram(id);},error:()=>this.toast.error('Não foi possível atualizar o odontograma.')});}
  editProcedure(procedure:Procedure):void {this.selectedResourceId.set(procedure.id);this.procedureForm.setValue({name:procedure.name,description:procedure.description??'',price:procedure.price,durationMinutes:procedure.durationMinutes,active:procedure.active});this.formOpen.set(true);}
  saveProcedure():void {if(this.procedureForm.invalid)return;const request=this.selectedResourceId()?this.api.put(`procedimentos/${this.selectedResourceId()}`,this.procedureForm.getRawValue()):this.api.post('procedimentos',this.procedureForm.getRawValue());request.subscribe({next:()=>{this.formOpen.set(false);this.toast.success('Procedimento salvo.');this.loadProcedures();},error:()=>this.toast.error('Não foi possível salvar o procedimento.')});}
  deactivateProcedure(id:number):void {this.api.delete(`procedimentos/${id}`).subscribe({next:()=>{this.toast.success('Procedimento inativado.');this.loadProcedures();},error:()=>this.toast.error('Não foi possível inativar.')});}
  savePayment():void {if(this.paymentForm.invalid)return;const value=this.paymentForm.getRawValue();this.api.post('pagamentos',{...value,procedureId:value.procedureId||null}).subscribe({next:()=>{this.formOpen.set(false);this.toast.success('Pagamento registrado.');this.loadSection();},error:()=>this.toast.error('Não foi possível registrar o pagamento.')});}
  saveQuote():void {if(this.quoteForm.invalid)return;const value=this.quoteForm.getRawValue();this.api.post('orcamentos',{patientId:value.patientId,items:[{procedureId:value.procedureId,quantity:value.quantity}],discount:value.discount,notes:value.notes}).subscribe({next:()=>{this.formOpen.set(false);this.toast.success('Orçamento criado.');this.loadSection();},error:error=>this.toast.error(error.error?.message||'Não foi possível criar o orçamento.')});}
  markStatus(id:number,status:string):void {this.api.put(`orcamentos/${id}/status`,{status}).subscribe({next:()=>{this.toast.success('Status do orçamento atualizado.');this.loadSection();},error:()=>this.toast.error('Não foi possível atualizar o orçamento.')});}
  private resetForms():void {this.selectedPatientId.set(null);this.selectedResourceId.set(null);this.patientForm.reset({name:'',cpf:'',birthDate:'',gender:'',phone:'',email:'',address:'',notes:''});this.dentistForm.reset({name:'',cro:'',specialty:'',phone:'',email:'',active:true});this.appointmentForm.reset({patientId:0,dentistId:0,startsAt:'',endsAt:'',type:'',notes:'',status:'AGENDADA'});this.procedureForm.reset({name:'',description:'',price:0,durationMinutes:30,active:true});}
  private saveFailed():void{this.saving.set(false);this.toast.error('Não foi possível salvar. Verifique os dados informados.');}
  private failed():void{this.loading.set(false);this.toast.error('Falha ao carregar dados. Verifique a conexão com a API.');}
  private localDate(date:Date):string{return `${date.getFullYear()}-${String(date.getMonth()+1).padStart(2,'0')}-${String(date.getDate()).padStart(2,'0')}`;}
  private localNow():string{const d=new Date();return `${this.localDate(d)}T${String(d.getHours()).padStart(2,'0')}:${String(d.getMinutes()).padStart(2,'0')}`;}
}
