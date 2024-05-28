-- Create EMPLOYEE table
CREATE TABLE EMPLOYEE (
    id LONG AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    email VARCHAR(100),
    date_of_birth DATE,
    position VARCHAR(100),
    salary DECIMAL(10, 2),
    service_years INT,
    address VARCHAR(255),
    sin_number VARCHAR(100),
    driver_licence_number VARCHAR(100),
    status VARCHAR(50),
    create_datetime DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_datetime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE EMPLOYEE_AUDIT (
    audit_id LONG AUTO_INCREMENT PRIMARY KEY,
    employee_id LONG,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    email VARCHAR(100),
    date_of_birth DATE,
    position VARCHAR(100),
    salary DECIMAL(10, 2),
    service_years INT,
    address VARCHAR(255),
    sin_number VARCHAR(100),
    driver_licence_number VARCHAR(100),
    status VARCHAR(50),
    operation_username VARCHAR(100),
    operation_type VARCHAR(10),
    operation_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES EMPLOYEE(id)
);

-- ------------------------------ Database Level Triggers ------------------------------ (does not work in H2, just FYI)
-- CREATE TRIGGER after_employee_insert
-- AFTER INSERT ON EMPLOYEE
-- FOR EACH ROW
-- BEGIN
--     INSERT INTO EMPLOYEE_AUDIT (
--         employee_id, first_name, last_name, email, date_of_birth, position, salary,
--         service_years, address, sin_number, driver_licence_number, status, operation_username, operation_type
--     ) VALUES (
--         NEW.id, NEW.first_name, NEW.last_name, NEW.email, NEW.date_of_birth, NEW.position, NEW.salary,
--         NEW.service_years, NEW.address, NEW.sin_number, NEW.driver_licence_number, NEW.status, 'ADMIN', 'INSERT'
--     );
-- END;

-- CREATE TRIGGER after_employee_update
-- AFTER UPDATE ON EMPLOYEE
-- FOR EACH ROW
-- BEGIN
--     INSERT INTO EMPLOYEE_AUDIT (
--         employee_id, first_name, last_name, email, date_of_birth, position, salary,
--         service_years, address, sin_number, driver_licence_number, status, operation_username, operation_type
--     ) VALUES (
--         NEW.id, NEW.first_name, NEW.last_name, NEW.email, NEW.date_of_birth, NEW.position, NEW.salary,
--         NEW.service_years, NEW.address, NEW.sin_number, NEW.driver_licence_number, NEW.status, 'ADMIN', 'UPDATE'
--     );
-- END;

-- Insert dummy data into EMPLOYEE table
INSERT INTO EMPLOYEE (first_name, last_name, email, date_of_birth, salary, service_years, address, sin_number, driver_licence_number, position, status) VALUES
('John', 'Doe', 'john.doe1@example.com', '1985-07-14', 50000.00, 5, '123 Elm St, Springfield, SP', '123456789', 'D1234567', 'ADMIN', 'ACTIVE'),
('Jane', 'Smith', 'jane.smith2@example.com', '1990-08-05', 55000.00, 3, '456 Oak St, Metropolis, MP', '234567890', 'D2345678', 'MANAGER', 'INACTIVE'),
('Alice', 'Johnson', 'alice.johnson3@example.com', '1983-11-22', 60000.00, 7, '789 Pine St, Gotham, GH', '345678901', 'D3456789', 'DEVELOPER', 'ACTIVE'),
('Bob', 'Williams', 'bob.williams4@example.com', '1975-04-09', 62000.00, 10, '321 Maple St, Star City, SC', '456789012', 'D4567890', 'BA', 'ACTIVE'),
('Carol', 'Brown', 'carol.brown5@example.com', '1992-03-15', 45000.00, 2, '654 Spruce St, Central City, CC', '567890123', 'D5678901', 'QA', 'INACTIVE'),
('Dave', 'Jones', 'dave.jones6@example.com', '1988-12-08', 47000.00, 4, '987 Cedar St, Smallville, SV', '678901234', 'D6789012', 'HR', 'ACTIVE'),
('Eve', 'Miller', 'eve.miller7@example.com', '1980-01-29', 70000.00, 12, '246 Birch St, Atlantis, AT', '789012345', 'D7890123', 'FINANCE', 'INACTIVE'),
('Frank', 'Wilson', 'frank.wilson8@example.com', '1972-05-16', 73000.00, 15, '135 Willow St, Themyscira, TM', '890123456', 'D8901234', 'MARKETING', 'ACTIVE'),
('Grace', 'Moore', 'grace.moore9@example.com', '1995-07-30', 54000.00, 1, '864 Elm St, Paradise Island, PI', '901234567', 'D9012345', 'SALES', 'INACTIVE'),
('Henry', 'Taylor', 'henry.taylor10@example.com', '1987-09-17', 61000.00, 6, '975 Oak St, Keystone City, KC', '012345678', 'D0123456', 'RETIRED', 'ACTIVE'),
('Ian', 'Harris', 'ian.harris11@example.com', '1991-01-14', 59000.00, 4, '123 Birch St, Metro City, MC', '098765432', 'E1098765', 'FINANCE', 'ACTIVE'),
('Julia', 'Morales', 'julia.morales12@example.com', '1988-02-23', 65000.00, 8, '234 Pine St, New Town, NT', '123098456', 'F2109876', 'HR', 'INACTIVE'),
('Kyle', 'Anderson', 'kyle.anderson13@example.com', '1974-03-11', 55000.00, 10, '345 Maple St, Old Town, OT', '234109567', 'G3210987', 'DEVELOPER', 'ACTIVE'),
('Linda', 'Martinez', 'linda.martinez14@example.com', '1990-04-28', 63000.00, 5, '456 Elm St, Springfield, SP', '345120678', 'H4321098', 'MARKETING', 'INACTIVE'),
('Mark', 'Lewis', 'mark.lewis15@example.com', '1986-05-15', 47000.00, 3, '567 Oak St, Metropolis, MP', '456231789', 'I5432109', 'QA', 'ACTIVE'),
('Nina', 'Sanchez', 'nina.sanchez16@example.com', '1993-06-30', 48000.00, 2, '678 Pine St, Gotham, GH', '567342890', 'J6543210', 'SALES', 'INACTIVE'),
('Oscar', 'Clark', 'oscar.clark17@example.com', '1980-07-25', 71000.00, 12, '789 Maple St, Star City, SC', '678453901', 'K7654321', 'BA', 'ACTIVE'),
('Paula', 'Rodriguez', 'paula.rodriguez18@example.com', '1971-08-20', 73000.00, 15, '890 Elm St, Central City, CC', '789564012', 'L8765432', 'ADMIN', 'INACTIVE'),
('Quinn', 'Lopez', 'quinn.lopez19@example.com', '1995-09-13', 53000.00, 1, '901 Cedar St, Smallville, SV', '890675123', 'M9876543', 'MANAGER', 'ACTIVE'),
('Rita', 'White', 'rita.white20@example.com', '1989-10-22', 60000.00, 7, '012 Birch St, Atlantis, AT', '901786234', 'N0987654', 'HR', 'INACTIVE'),
-- Entries 21-40
('Simon', 'Green', 'simon.green21@example.com', '1987-03-12', 52000.00, 6, '123 Spruce St, Keystone City, KC', '101112233', 'O1098765', 'DEVELOPER', 'ACTIVE'),
('Tina', 'Adams', 'tina.adams22@example.com', '1989-06-18', 55000.00, 5, '234 Cedar St, Paradise Island, PI', '202223344', 'P2109876', 'BA', 'INACTIVE'),
('Uma', 'Nelson', 'uma.nelson23@example.com', '1982-09-22', 67000.00, 9, '345 Birch St, Metro City, MC', '303334455', 'Q3210987', 'HR', 'ACTIVE'),
('Victor', 'Mitchell', 'victor.mitchell24@example.com', '1991-12-11', 49000.00, 3, '456 Pine St, New Town, NT', '404445566', 'R4321098', 'FINANCE', 'INACTIVE'),
('Wendy', 'Perez', 'wendy.perez25@example.com', '1978-05-20', 51000.00, 7, '567 Maple St, Old Town, OT', '505556677', 'S5432109', 'MARKETING', 'ACTIVE'),
('Xavier', 'Roberts', 'xavier.roberts26@example.com', '1996-07-29', 56000.00, 2, '678 Elm St, Springfield, SP', '606667788', 'T6543210', 'QA', 'INACTIVE'),
('Yolanda', 'Edwards', 'yolanda.edwards27@example.com', '1983-08-16', 58000.00, 8, '789 Oak St, Metropolis, MP', '707778899', 'U7654321', 'SALES', 'ACTIVE'),
('Zachary', 'Collins', 'zachary.collins28@example.com', '1974-10-04', 60000.00, 10, '890 Pine St, Gotham, GH', '808889900', 'V8765432', 'RETIRED', 'INACTIVE'),
('Amy', 'Howard', 'amy.howard29@example.com', '1986-11-30', 62000.00, 4, '901 Maple St, Star City, SC', '909990011', 'W9876543', 'ADMIN', 'ACTIVE'),
('Brian', 'Evans', 'brian.evans30@example.com', '1992-01-15', 47000.00, 5, '012 Elm St, Central City, CC', '010111223', 'X0987654', 'MANAGER', 'INACTIVE'),
('Carmen', 'Ward', 'carmen.ward31@example.com', '1980-02-09', 53000.00, 7, '123 Cedar St, Smallville, SV', '121222334', 'Y1098765', 'DEVELOPER', 'ACTIVE'),
('David', 'Torres', 'david.torres32@example.com', '1979-03-18', 55000.00, 6, '234 Birch St, Atlantis, AT', '232333445', 'Z2109876', 'BA', 'INACTIVE'),
('Elena', 'Peterson', 'elena.peterson33@example.com', '1985-04-20', 49000.00, 9, '345 Spruce St, Keystone City, KC', '343444556', 'A3210987', 'HR', 'ACTIVE'),
('Floyd', 'Gibson', 'floyd.gibson34@example.com', '1993-05-22', 70000.00, 3, '456 Cedar St, Paradise Island, PI', '454555667', 'B4321098', 'FINANCE', 'INACTIVE'),
('Gina', 'Sanders', 'gina.sanders35@example.com', '1977-06-24', 73000.00, 15, '567 Birch St, Metro City, MC', '565666778', 'C5432109', 'MARKETING', 'ACTIVE'),
('Hugo', 'Martinez', 'hugo.martinez36@example.com', '1990-07-27', 54000.00, 1, '678 Pine St, New Town, NT', '676777889', 'D6543210', 'QA', 'INACTIVE'),
('Iris', 'Murphy', 'iris.murphy37@example.com', '1984-08-29', 61000.00, 6, '789 Maple St, Old Town, OT', '787888900', 'E7654321', 'SALES', 'ACTIVE'),
('Jason', 'Rivera', 'jason.rivera38@example.com', '1976-09-30', 46000.00, 8, '890 Elm St, Springfield, SP', '898999011', 'F8765432', 'RETIRED', 'INACTIVE'),
('Kathy', 'Coleman', 'kathy.coleman39@example.com', '1994-10-31', 55000.00, 4, '901 Oak St, Metropolis, MP', '900000122', 'G9876543', 'ADMIN', 'ACTIVE'),
('Luis', 'Phillips', 'luis.phillips40@example.com', '1991-11-12', 59000.00, 5, '012 Pine St, Gotham, GH', '011111234', 'H0987654', 'MANAGER', 'INACTIVE'),
-- Entries 41-60
('Mia', 'Powell', 'mia.powell41@example.com', '1982-12-13', 52000.00, 8, '123 Cedar St, Star City, SC', '112233445', 'I1098765', 'DEVELOPER', 'ACTIVE'),
('Noah', 'Russell', 'noah.russell42@example.com', '1995-01-14', 56000.00, 2, '234 Birch St, Central City, CC', '223344556', 'J2109876', 'BA', 'INACTIVE'),
('Olivia', 'Carter', 'olivia.carter43@example.com', '1989-02-16', 49000.00, 6, '345 Spruce St, Smallville, SV', '334455667', 'K3210987', 'HR', 'ACTIVE'),
('Pablo', 'Reed', 'pablo.reed44@example.com', '1978-03-18', 53000.00, 10, '456 Cedar St, Atlantis, AT', '445566778', 'L4321098', 'FINANCE', 'INACTIVE'),
('Quincy', 'Perry', 'quincy.perry45@example.com', '1987-04-20', 67000.00, 9, '567 Maple St, Keystone City, KC', '556677889', 'M5432109', 'MARKETING', 'ACTIVE'),
('Rachel', 'Long', 'rachel.long46@example.com', '1996-05-22', 58000.00, 3, '678 Elm St, Paradise Island, PI', '667788990', 'N6543210', 'QA', 'INACTIVE'),
('Steve', 'Patterson', 'steve.patterson47@example.com', '1983-06-24', 60000.00, 7, '789 Oak St, Metro City, MC', '778899001', 'O7654321', 'SALES', 'ACTIVE'),
('Teresa', 'Hughes', 'teresa.hughes48@example.com', '1974-07-26', 73000.00, 12, '890 Pine St, New Town, NT', '889900112', 'P8765432', 'RETIRED', 'INACTIVE'),
('Ulysses', 'Foster', 'ulysses.foster49@example.com', '1986-08-28', 55000.00, 5, '901 Birch St, Old Town, OT', '990011223', 'Q9876543', 'ADMIN', 'ACTIVE'),
('Violet', 'Jimenez', 'violet.jimenez50@example.com', '1990-09-29', 47000.00, 4, '012 Spruce St, Springfield, SP', '100122334', 'R0987654', 'MANAGER', 'INACTIVE'),
('Walter', 'Bryant', 'walter.bryant51@example.com', '1981-10-30', 48000.00, 6, '123 Cedar St, Central City, CC', '211233445', 'S1098765', 'DEVELOPER', 'ACTIVE'),
('Xenia', 'Watson', 'xenia.watson52@example.com', '1994-11-01', 63000.00, 5, '234 Birch St, Smallville, SV', '322344556', 'T2109876', 'BA', 'INACTIVE'),
('Yasmin', 'Bradley', 'yasmin.bradley53@example.com', '1985-12-03', 61000.00, 11, '345 Cedar St, Atlantis, AT', '433455667', 'U3210987', 'HR', 'ACTIVE'),
('Zane', 'Gonzalez', 'zane.gonzalez54@example.com', '1977-01-05', 54000.00, 8, '456 Maple St, Keystone City, KC', '544566778', 'V4321098', 'FINANCE', 'INACTIVE'),
('Abigail', 'Morales', 'abigail.morales55@example.com', '1991-02-06', 57000.00, 9, '567 Elm St, Paradise Island, PI', '655677889', 'W5432109', 'MARKETING', 'ACTIVE'),
('Benjamin', 'Ortiz', 'benjamin.ortiz56@example.com', '1993-03-08', 46000.00, 3, '678 Oak St, Metro City, MC', '766788990', 'X6543210', 'QA', 'INACTIVE'),
('Carmen', 'Reyes', 'carmen.reyes57@example.com', '1984-04-09', 52000.00, 6, '789 Pine St, New Town, NT', '877899001', 'Y7654321', 'SALES', 'ACTIVE'),
('Dexter', 'Jenkins', 'dexter.jenkins58@example.com', '1975-05-10', 69000.00, 10, '890 Spruce St, Old Town, OT', '988900112', 'Z8765432', 'RETIRED', 'INACTIVE'),
('Eleanor', 'Burns', 'eleanor.burns59@example.com', '1986-06-11', 50000.00, 4, '901 Birch St, Springfield, SP', '099011223', 'A9876543', 'ADMIN', 'ACTIVE'),
('Franklin', 'Hansen', 'franklin.hansen60@example.com', '1992-07-12', 51000.00, 5, '012 Cedar St, Central City, CC', '110122334', 'B0987654', 'MANAGER', 'INACTIVE');