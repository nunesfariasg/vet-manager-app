/**
 * Author:  nunes
 * Created: Sep 26, 2026
 */

CREATE DATABASE vet_manager_db;

create table users (
id serial primary key,
username varchar(50) not null,
password varchar(8) not null
);

create table clients (
id serial primary key,
name varchar(50) not null,
cpf varchar(14) unique,
phone varchar(20),
email varchar(100)
);

create table animals (
id serial primary key,
name varchar(50) not null,
species varchar(50) not null,
breed varchar(50) not null,
age int not null,
cliente_id int not null,
foreign key (cliente_id) references clients(id) on delete cascade
);

create table appointments (
id serial primary key,
animal_id int not null,
date_appointment date not null, 
time_appointment time not null,
reson varchar(50) not null,
notes varchar(256),
foreign key (animal_id) references animals(id) on delete cascade 
);
