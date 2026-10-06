CREATE TABLE app_user (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(120) NOT NULL,
  email VARCHAR(180) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  role VARCHAR(24) NOT NULL,
  enabled BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE dentist (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(120) NOT NULL,
  cro VARCHAR(40) NOT NULL UNIQUE,
  specialty VARCHAR(100) NOT NULL,
  phone VARCHAR(30),
  email VARCHAR(180) NOT NULL UNIQUE,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE patient (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(140) NOT NULL,
  cpf VARCHAR(14) NOT NULL UNIQUE,
  birth_date DATE,
  gender VARCHAR(24),
  phone VARCHAR(30),
  email VARCHAR(180),
  address VARCHAR(255),
  notes TEXT,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE appointment (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  patient_id BIGINT NOT NULL,
  dentist_id BIGINT NOT NULL,
  starts_at DATETIME NOT NULL,
  ends_at DATETIME NOT NULL,
  appointment_type VARCHAR(120) NOT NULL,
  notes TEXT,
  status VARCHAR(24) NOT NULL,
  CONSTRAINT fk_appointment_patient FOREIGN KEY (patient_id) REFERENCES patient(id),
  CONSTRAINT fk_appointment_dentist FOREIGN KEY (dentist_id) REFERENCES dentist(id),
  INDEX idx_appointment_starts_at (starts_at),
  INDEX idx_appointment_dentist_time (dentist_id, starts_at, ends_at)
);
CREATE TABLE clinical_record (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  patient_id BIGINT NOT NULL UNIQUE,
  chief_complaint TEXT,
  anamnesis TEXT,
  diagnosis TEXT,
  observations TEXT,
  procedures TEXT,
  medications TEXT,
  allergies TEXT,
  dental_history TEXT,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_record_patient FOREIGN KEY (patient_id) REFERENCES patient(id)
);
CREATE TABLE dental_procedure (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(120) NOT NULL UNIQUE,
  description TEXT,
  price DECIMAL(12,2) NOT NULL,
  duration_minutes INT NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE
);
CREATE TABLE quote (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  patient_id BIGINT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  discount DECIMAL(12,2) NOT NULL DEFAULT 0,
  total DECIMAL(12,2) NOT NULL,
  status VARCHAR(24) NOT NULL,
  notes TEXT,
  CONSTRAINT fk_quote_patient FOREIGN KEY (patient_id) REFERENCES patient(id)
);
CREATE TABLE quote_item (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  quote_id BIGINT NOT NULL,
  procedure_id BIGINT NOT NULL,
  quantity INT NOT NULL,
  unit_price DECIMAL(12,2) NOT NULL,
  CONSTRAINT fk_quote_item_quote FOREIGN KEY (quote_id) REFERENCES quote(id),
  CONSTRAINT fk_quote_item_procedure FOREIGN KEY (procedure_id) REFERENCES dental_procedure(id)
);
CREATE TABLE payment (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  patient_id BIGINT NOT NULL,
  procedure_id BIGINT,
  quote_id BIGINT,
  amount DECIMAL(12,2) NOT NULL,
  paid_at DATETIME NOT NULL,
  method VARCHAR(24) NOT NULL,
  status VARCHAR(24) NOT NULL,
  description VARCHAR(255),
  CONSTRAINT fk_payment_patient FOREIGN KEY (patient_id) REFERENCES patient(id),
  CONSTRAINT fk_payment_procedure FOREIGN KEY (procedure_id) REFERENCES dental_procedure(id),
  CONSTRAINT fk_payment_quote FOREIGN KEY (quote_id) REFERENCES quote(id)
);
CREATE TABLE odontogram_entry (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  patient_id BIGINT NOT NULL,
  tooth_number VARCHAR(3) NOT NULL,
  condition_type VARCHAR(24) NOT NULL,
  observation TEXT,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_odontogram_patient FOREIGN KEY (patient_id) REFERENCES patient(id),
  UNIQUE KEY uk_odontogram_patient_tooth (patient_id, tooth_number)
);
