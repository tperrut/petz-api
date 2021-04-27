# Add constraint
alter table pet add constraint fk_cliente_pet foreign key (id_dono_fk) references cliente (id);



