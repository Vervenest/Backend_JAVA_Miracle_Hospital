USE miracledb;

-- ================================================================
-- TABLE 1: adminusers
-- ================================================================
CREATE TABLE `adminusers` (
  `id`              INT          NOT NULL AUTO_INCREMENT,
  `adminId`         VARCHAR(64)  NOT NULL,
  `adminName`       VARCHAR(250) NOT NULL,
  `adminEmail`      VARCHAR(100) NOT NULL,
  `adminPassword`   VARCHAR(100) NOT NULL,
  `adminStatus`     VARCHAR(5)   NOT NULL DEFAULT '1',
  `create_datetime` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_datetime` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `adminusers` (`adminId`,`adminName`,`adminEmail`,`adminPassword`,`adminStatus`)
VALUES ('admin123123','Admin','admin@gmail.com','123456','1');

-- ================================================================
-- TABLE 2: users
-- ================================================================
CREATE TABLE `users` (
  `id`           INT          NOT NULL AUTO_INCREMENT,
  `userId`       VARCHAR(50)  DEFAULT NULL,
  `token_id`     VARCHAR(255) NOT NULL DEFAULT '',
  `otp`          VARCHAR(10)  NOT NULL DEFAULT '',
  `userName`     VARCHAR(100) DEFAULT NULL,
  `userPhone`    VARCHAR(15)  DEFAULT NULL,
  `phone_number` VARCHAR(15)  DEFAULT NULL,
  `email`        VARCHAR(150) DEFAULT NULL,
  `password`     VARCHAR(255) DEFAULT NULL,
  `userRole`     ENUM('ADMIN','DOCTOR','PATIENT') DEFAULT NULL,
  `is_active`    TINYINT(1)   DEFAULT '1',
  `server_mode`  VARCHAR(50)  NOT NULL DEFAULT 'PRODUCTION',
  `user_img`     VARCHAR(255) DEFAULT NULL,
  `created_at`   DATETIME     DEFAULT CURRENT_TIMESTAMP,
  `updated_at`   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_userId`    (`userId`),
  UNIQUE KEY `uq_userPhone` (`userPhone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `users` (`userId`,`userName`,`userPhone`,`phone_number`,`otp`,`userRole`,`is_active`,`server_mode`)
VALUES ('user001','Ravi Kumar','9876543210','9876543210','1234','PATIENT','1','DEVELOPMENT');

INSERT INTO `users` (`userId`,`userName`,`userPhone`,`phone_number`,`otp`,`userRole`,`is_active`,`server_mode`)
VALUES ('user002','Dr. Priya Sharma','9123456780','9123456780','1234','DOCTOR','1','DEVELOPMENT');

-- ================================================================
-- TABLE 3: patients
-- ================================================================
CREATE TABLE `patients` (
  `id`                  INT          NOT NULL AUTO_INCREMENT,
  `patientId`           VARCHAR(50)  DEFAULT NULL,
  `userId`              VARCHAR(50)  DEFAULT NULL,
  `user_id`             INT          DEFAULT NULL,
  `patientName`         VARCHAR(100) DEFAULT NULL,
  `patientRelation`     VARCHAR(50)  DEFAULT NULL,
  `patientPhone`        VARCHAR(15)  DEFAULT NULL,
  `patientGender`       VARCHAR(50)  NOT NULL DEFAULT '',
  `patientDateOfBirth`  VARCHAR(50)  NOT NULL DEFAULT '',
  `patientAge`          VARCHAR(10)  NOT NULL DEFAULT '',
  `patientStatus`       VARCHAR(10)  DEFAULT '1',
  `date_of_birth`       DATE         DEFAULT NULL,
  `gender`              ENUM('MALE','FEMALE','OTHER') DEFAULT NULL,
  `address`             VARCHAR(500) DEFAULT NULL,
  `blood_group`         VARCHAR(10)  DEFAULT NULL,
  `emergency_contact`   VARCHAR(20)  DEFAULT NULL,
  `medical_history`     TEXT         DEFAULT NULL,
  `allergies`           TEXT         DEFAULT NULL,
  `insurance_provider`  VARCHAR(100) DEFAULT NULL,
  `insurance_number`    VARCHAR(100) DEFAULT NULL,
  `is_active`           TINYINT(1)   DEFAULT '1',
  `created_at`          DATETIME     DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_userId`  (`userId`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `fk_patients_users` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `patients` (`patientId`,`userId`,`user_id`,`patientName`,`patientRelation`,`patientPhone`,`patientGender`,`patientDateOfBirth`,`patientAge`,`patientStatus`,`is_active`)
VALUES ('pat001','user001',1,'Ravi Kumar','Self','9876543210','Male','1990-05-15','34','1',1);

-- ================================================================
-- TABLE 4: doctor
-- ================================================================
CREATE TABLE `doctor` (
  `id`                   INT          NOT NULL AUTO_INCREMENT,
  `doctorId`             VARCHAR(64)  DEFAULT NULL,
  `user_id`              INT          DEFAULT NULL,
  `token_id`             TEXT         NOT NULL DEFAULT '',
  `doctorName`           VARCHAR(150) NOT NULL DEFAULT '',
  `doctorPhone`          VARCHAR(20)  NOT NULL DEFAULT '',
  `doctorProfileImg`     VARCHAR(250) NOT NULL DEFAULT '',
  `doctorSpecialisation` VARCHAR(250) NOT NULL DEFAULT '',
  `doctorShortDesc`      TEXT         NOT NULL,
  `scanType`             VARCHAR(255) NOT NULL DEFAULT '',
  `doctorStatus`         VARCHAR(5)   NOT NULL DEFAULT '1',
  `isAdminDoctor`        VARCHAR(64)  NOT NULL DEFAULT 'NO',
  `otp`                  VARCHAR(10)  NOT NULL DEFAULT '1234',
  `server_mode`          VARCHAR(50)  NOT NULL DEFAULT 'PRODUCTION',
  `qualifications`       VARCHAR(500) DEFAULT NULL,
  `experience_years`     INT          DEFAULT NULL,
  `consultation_fee`     DOUBLE       DEFAULT NULL,
  `clinic_address`       VARCHAR(500) DEFAULT NULL,
  `clinic_phone`         VARCHAR(20)  DEFAULT NULL,
  `availability_days`    VARCHAR(100) DEFAULT NULL,
  `availability_time`    VARCHAR(100) DEFAULT NULL,
  `is_verified`          TINYINT(1)   DEFAULT '0',
  `is_active`            TINYINT(1)   DEFAULT '1',
  `create_datetime`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_datetime`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `fk_doctor_users` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `doctor` (`doctorId`,`user_id`,`token_id`,`doctorName`,`doctorPhone`,`doctorProfileImg`,`doctorSpecialisation`,`doctorShortDesc`,`doctorStatus`,`isAdminDoctor`,`is_active`,`is_verified`)
VALUES ('doc001',2,'','Dr. Priya Sharma','9123456780','','d0013b0263e6426f','Experienced Cardiologist with 10 years of practice','1','YES',1,1);

-- ================================================================
-- TABLE 5: doctorspecialisation
-- ================================================================
CREATE TABLE `doctorspecialisation` (
  `id`                   INT          NOT NULL AUTO_INCREMENT,
  `specialisationId`     VARCHAR(64)  NOT NULL,
  `specialisationName`   VARCHAR(150) NOT NULL,
  `specialisationStatus` VARCHAR(5)   NOT NULL DEFAULT '1',
  `create_datetime`      DATETIME(6)  DEFAULT NULL,
  `update_datetime`      DATETIME(6)  DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `doctorspecialisation` (`specialisationId`,`specialisationName`,`specialisationStatus`) VALUES
('c80ccb3da3dff213','Ortho','1'),
('513f11c2fbad5006','Pediatric','1'),
('efe45607b90ce3e7','Dental','1'),
('d0013b0263e6426f','Cardiology','1'),
('31fa3faf7542427b','Gynic','1'),
('3193d4a45f88f6a1','Optho','1'),
('6cbac6b56a858696','General medicine','1'),
('106a03e3759ee8ec','Pediatrician and Neonatologist','1'),
('c87525813f7cb068','Gynecologist','1'),
('a83aad82055e97f9','Dermatology','1'),
('ee5573f169392764','ENT','1'),
('52562424ad68ff3f','RADIOLOGIST','1'),
('83d1a8a4d5bf35a9','Neurologist','1');

-- ================================================================
-- TABLE 6: doctorAvailableSlotTime
-- ================================================================
CREATE TABLE `doctorAvailableSlotTime` (
  `id`         INT          NOT NULL AUTO_INCREMENT,
  `doctorId`   VARCHAR(64)  NOT NULL,
  `dayOfWeek`  ENUM('Monday','Tuesday','Wednesday','Thursday','Friday','Saturday','Sunday') NOT NULL,
  `startTime`  TIME         DEFAULT NULL,
  `endTime`    TIME         DEFAULT NULL,
  `isLeave`    TINYINT(1)   DEFAULT '0',
  `remarks`    VARCHAR(255) DEFAULT NULL,
  `created_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_doctor_day` (`doctorId`,`dayOfWeek`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `doctorAvailableSlotTime` (`doctorId`,`dayOfWeek`,`startTime`,`endTime`,`isLeave`) VALUES
('doc001','Monday',   '09:00:00','20:00:00',0),
('doc001','Tuesday',  '09:00:00','20:00:00',0),
('doc001','Wednesday','09:00:00','20:00:00',0),
('doc001','Thursday', '09:00:00','20:00:00',0),
('doc001','Friday',   '09:00:00','20:00:00',0),
('doc001','Saturday', NULL,      NULL,       1),
('doc001','Sunday',   NULL,      NULL,       1);

-- ================================================================
-- TABLE 7: doctor_timeslots
-- ================================================================
CREATE TABLE `doctor_timeslots` (
  `id`          BIGINT      NOT NULL AUTO_INCREMENT,
  `doctor_id`   BIGINT      NOT NULL,
  `day_of_week` VARCHAR(20) NOT NULL,
  `start_time`  VARCHAR(10) DEFAULT NULL,
  `end_time`    VARCHAR(10) DEFAULT NULL,
  `is_leave`    INT         NOT NULL DEFAULT '0',
  `createdAt`   DATETIME    NOT NULL,
  `updatedAt`   DATETIME    NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_doctor_day` (`doctor_id`,`day_of_week`),
  CONSTRAINT `fk_timeslot_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `doctor`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ================================================================
-- TABLE 8: appointment
-- ================================================================
CREATE TABLE `appointment` (
  `id`                    INT          NOT NULL AUTO_INCREMENT,
  `patient_id`            INT          DEFAULT NULL,
  `doctor_id`             INT          DEFAULT NULL,
  `appointmentId`         VARCHAR(64)  NOT NULL DEFAULT '',
  `todayTokenNo`          VARCHAR(5)   NOT NULL DEFAULT '',
  `doctorId`              VARCHAR(64)  NOT NULL DEFAULT '',
  `userId`                VARCHAR(255) NOT NULL DEFAULT '',
  `patientId`             VARCHAR(50)  DEFAULT NULL,
  `appointmentDate`       VARCHAR(64)  NOT NULL DEFAULT '',
  `appointmentStartTime`  VARCHAR(64)  NOT NULL DEFAULT '',
  `appointmentEndTime`    VARCHAR(64)  NOT NULL DEFAULT '',
  `appointmentStatus`     VARCHAR(10)  NOT NULL DEFAULT '1',
  `delayTime`             VARCHAR(64)  NOT NULL DEFAULT '',
  `appointmentTime`       VARCHAR(50)  NOT NULL DEFAULT '',
  `appointmentDuration`   VARCHAR(50)  NOT NULL DEFAULT '',
  `appointmentReason`     MEDIUMTEXT   NOT NULL,
  `scanType`              VARCHAR(255) DEFAULT NULL,
  `appointment_date_time` DATETIME     DEFAULT NULL,
  `reason_for_visit`      VARCHAR(500) DEFAULT NULL,
  `notes`                 TEXT         DEFAULT NULL,
  `status`                ENUM('SCHEDULED','COMPLETED','CANCELLED','NO_SHOW','RESCHEDULED') DEFAULT 'SCHEDULED',
  `create_datetime`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_datetime`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_patient_id` (`patient_id`),
  KEY `idx_doctor_id`  (`doctor_id`),
  CONSTRAINT `fk_appt_patient` FOREIGN KEY (`patient_id`) REFERENCES `patients`(`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_appt_doctor`  FOREIGN KEY (`doctor_id`)  REFERENCES `doctor`(`id`)   ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `appointment`
  (`patient_id`,`doctor_id`,`appointmentId`,`todayTokenNo`,`doctorId`,`userId`,`patientId`,
   `appointmentDate`,`appointmentStartTime`,`appointmentEndTime`,`appointmentStatus`,`appointmentReason`,`status`)
VALUES
  (1,1,'appt001','1','doc001','user001','pat001',
   '2026-02-27','10:00','10:30','1','Routine checkup','SCHEDULED');

-- ================================================================
-- TABLE 9: notification
-- ================================================================
CREATE TABLE `notification` (
  `notificationId`    BIGINT       NOT NULL AUTO_INCREMENT,
  `user_id`           INT          NOT NULL,
  `title`             VARCHAR(250) NOT NULL,
  `message`           TEXT         NOT NULL,
  `notification_type` ENUM('APPOINTMENT_REMINDER','APPOINTMENT_CONFIRMATION','APPOINTMENT_CANCELLED',
                           'PRESCRIPTION_READY','TEST_RESULT_AVAILABLE','MESSAGE','SYSTEM_NOTIFICATION') DEFAULT NULL,
  `fcm_token`         VARCHAR(500) DEFAULT NULL,
  `is_read`           TINYINT(1)   NOT NULL DEFAULT '0',
  `related_id`        BIGINT       DEFAULT NULL,
  `createdAt`         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`notificationId`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `fk_notification_user` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ================================================================
-- TABLE 10: notifications
-- ================================================================
CREATE TABLE `notifications` (
  `notificationId`    BIGINT       NOT NULL AUTO_INCREMENT,
  `user_id`           INT          NOT NULL,
  `title`             VARCHAR(250) NOT NULL,
  `message`           TEXT         NOT NULL,
  `notification_type` ENUM('APPOINTMENT_REMINDER','APPOINTMENT_CONFIRMATION','APPOINTMENT_CANCELLED',
                           'PRESCRIPTION_READY','TEST_RESULT_AVAILABLE','MESSAGE','SYSTEM_NOTIFICATION') DEFAULT NULL,
  `fcm_token`         VARCHAR(500) DEFAULT NULL,
  `is_read`           TINYINT(1)   NOT NULL DEFAULT '0',
  `related_id`        BIGINT       DEFAULT NULL,
  `createdAt`         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`notificationId`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `fk_notifications_user` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ================================================================
-- TABLE 11: app_config
-- ================================================================
CREATE TABLE `app_config` (
  `id`              INT          NOT NULL AUTO_INCREMENT,
  `app_id`          VARCHAR(200) NOT NULL,
  `app_status`      VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
  `title`           VARCHAR(350) NOT NULL,
  `message`         TEXT         NOT NULL,
  `create_datetime` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_datetime` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_app_id` (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `app_config` (`app_id`,`app_status`,`title`,`message`) VALUES
('com.vervenest.patient','ACTIVE','Miracle Hospital Patient App','Welcome to Miracle Hospital'),
('com.vervenest.doctor', 'ACTIVE','Miracle Hospital Doctor App', 'Welcome to Miracle Hospital Doctor Portal');

-- ================================================================
-- TABLE 12: app_config_detail
-- ================================================================
CREATE TABLE `app_config_detail` (
  `detail_id`          BIGINT       NOT NULL AUTO_INCREMENT,
  `app_id`             VARCHAR(200) NOT NULL,
  `version_code`       INT          NOT NULL DEFAULT '0',
  `app_current_status` ENUM('ACTIVE','MAINTENANCE','DEPRECATED','FORCED_UPDATE') NOT NULL DEFAULT 'ACTIVE',
  `title`              VARCHAR(350) NOT NULL,
  `message`            TEXT         NOT NULL,
  `redirect_url`       VARCHAR(350) DEFAULT NULL,
  `delete_status`      INT          NOT NULL DEFAULT '0',
  `created_at`         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at`         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`detail_id`),
  KEY `idx_app_id` (`app_id`),
  CONSTRAINT `fk_config_detail_app` FOREIGN KEY (`app_id`) REFERENCES `app_config`(`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ================================================================
-- TABLE 13: patientDoctorChat
-- ================================================================
CREATE TABLE `patientDoctorChat` (
  `id`              INT          NOT NULL AUTO_INCREMENT,
  `chatId`          VARCHAR(64)  NOT NULL,
  `userId`          VARCHAR(255) NOT NULL,
  `senderId`        VARCHAR(64)  NOT NULL,
  `senderType`      VARCHAR(64)  NOT NULL,
  `receiverId`      VARCHAR(64)  NOT NULL,
  `receiverType`    VARCHAR(64)  NOT NULL,
  `message`         TEXT         NOT NULL,
  `messageStatus`   VARCHAR(10)  NOT NULL DEFAULT '0',
  `doctorId`        VARCHAR(64)  NOT NULL,
  `doctorName`      VARCHAR(255) NOT NULL DEFAULT '',
  `create_datetime` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_datetime` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ================================================================
-- TABLE 14: patientDocuments
-- ================================================================
CREATE TABLE `patientDocuments` (
  `id`            INT          NOT NULL AUTO_INCREMENT,
  `appointmentId` VARCHAR(50)  NOT NULL,
  `patientId`     VARCHAR(50)  NOT NULL,
  `doctorId`      VARCHAR(50)  DEFAULT NULL,
  `fileName`      VARCHAR(255) NOT NULL,
  `docType`       VARCHAR(100) NOT NULL,
  `uploaded_at`   DATETIME     DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ================================================================
-- TABLE 15: config_platform
-- ================================================================
CREATE TABLE `config_platform` (
  `id`           INT          NOT NULL AUTO_INCREMENT,
  `config_id`    VARCHAR(100) NOT NULL,
  `platform`     VARCHAR(100) NOT NULL,
  `version`      VARCHAR(100) NOT NULL,
  `version_code` VARCHAR(100) NOT NULL,
  `note`         VARCHAR(100) NOT NULL DEFAULT '',
  `create_date`  VARCHAR(100) NOT NULL,
  `update_date`  VARCHAR(100) NOT NULL,
  `status`       VARCHAR(100) NOT NULL DEFAULT '0',
  `isMust`       VARCHAR(100) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ================================================================
-- TABLE 16: whatsapp_logs
-- ================================================================
CREATE TABLE `whatsapp_logs` (
  `id`         INT         NOT NULL AUTO_INCREMENT,
  `mobile`     VARCHAR(20) NOT NULL,
  `message`    TEXT        NOT NULL,
  `response`   TEXT        DEFAULT NULL,
  `status`     VARCHAR(50) DEFAULT NULL,
  `created_at` DATETIME    DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ================================================================
-- VERIFY
-- ================================================================
SHOW TABLES;
