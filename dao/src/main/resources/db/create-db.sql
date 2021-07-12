DROP SCHEMA IF EXISTS `speaking_practice_app`;

CREATE SCHEMA IF NOT EXISTS `speaking_practice_app` DEFAULT CHARACTER SET utf8mb4 ;

CREATE TABLE IF NOT EXISTS `speaking_practice_app`.`roles` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `created` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated` TIMESTAMP,
  `status` VARCHAR(45) NOT NULL DEFAULT 'ACTIVE',

  `name` VARCHAR(45) NOT NULL,

  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  UNIQUE INDEX `name_UNIQUE`(`name` ASC) VISIBLE)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `speaking_practice_app`.`languages` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `created` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated` TIMESTAMP,
  `status` VARCHAR(45) NOT NULL DEFAULT 'ACTIVE',

  `name` VARCHAR(45) NOT NULL,

  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  UNIQUE INDEX `name_UNIQUE`(`name` ASC) VISIBLE)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `speaking_practice_app`.`countries` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `created` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated` TIMESTAMP,
  `status` VARCHAR(45) NOT NULL DEFAULT 'ACTIVE',

  `name` VARCHAR(45) NOT NULL,

  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  UNIQUE INDEX `name_UNIQUE`(`name` ASC) VISIBLE)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `speaking_practice_app`.`customers` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `created` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated` TIMESTAMP,
  `status` VARCHAR(45) NOT NULL DEFAULT 'ACTIVE',

  `username` VARCHAR(45) NOT NULL,
  `firstname` VARCHAR(45) NOT NULL,
  `lastname` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `native_language_id` INT NOT NULL,
  `learning_language_id` INT NOT NULL,
  `date_of_birth` DATE,
  `country_id` INT,

  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
  UNIQUE INDEX `username_UNIQUE`(`username` ASC) VISIBLE,
  FOREIGN KEY (native_language_id) REFERENCES languages(id),
  FOREIGN KEY (learning_language_id) REFERENCES languages(id),
  FOREIGN KEY (country_id) REFERENCES countries(id))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `speaking_practice_app`.`customer_roles` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `created` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated` TIMESTAMP,
  `status` VARCHAR(45) NOT NULL DEFAULT 'ACTIVE',

  `customer_id` INT NOT NULL,
  `role_id` INT NOT NULL,

  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  UNIQUE (customer_id, role_id),
  FOREIGN KEY (customer_id) REFERENCES customers(id),
  FOREIGN KEY (role_id) REFERENCES roles(id))
ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `speaking_practice_app`.`requests` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `created` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated` TIMESTAMP,
  `status` VARCHAR(45) NOT NULL DEFAULT 'ACTIVE',

  `initiator_id` INT NOT NULL,
  `acceptor_id` INT,
  `language_id` INT NOT NULL,
  `wished_start_time` DATE NOT NULL,
  `wished_end_time` DATE NOT NULL,
  `accepted_start_time` DATE,

  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  FOREIGN KEY (initiator_id) REFERENCES customers(id),
  FOREIGN KEY (acceptor_id) REFERENCES customers(id),
  FOREIGN KEY (language_id) REFERENCES languages(id))
ENGINE = InnoDB;

