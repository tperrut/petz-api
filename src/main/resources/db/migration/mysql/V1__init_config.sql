# Criação da tabela Cliente

create table  cliente  (
 id  bigint not null auto_increment, 
 data_nascimento  DATE, 
 email  varchar(255) not null, 
 nome  varchar(255) not null, 
 perfil  varchar(255) not null, 
 senha  varchar(255) not null, 
 primary key (id)
) engine=InnoDB; 


# Criação da tabela Pet

create table  pet  (
 id  bigint not null auto_increment, 
 data_nascimento  DATE, 
 nome  varchar(255) not null, 
 raca  varchar(255), 
 tipo  integer, 
 id_dono_fk  bigint, primary key (id)
 ) engine=InnoDB;

