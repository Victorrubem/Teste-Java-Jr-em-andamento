CREATE TABLE contatos ( 
	id 			INTEGER NOT NULL AUTO_INCREMENT,
	nome		VARCHAR(140) NOT NULL,
	ramal		INTEGER NOT NULL,
	gti_version INTEGER NOT NULL DEFAULT 0,
	INDEX IDX_contatos_nome (nome),
	INDEX IDX_contatos_ramal (ramal),
	PRIMARY KEY(id)
);

CREATE TABLE recados ( 
	id 			INTEGER NOT NULL AUTO_INCREMENT,
	idContato	INTEGER NOT NULL,
	horario		TIMESTAMP NOT NULL,
	texto		VARCHAR(255) NOT NULL,
	indLido		TINYINT(1) NOT NULL DEFAULT 0,
	gti_version INTEGER NOT NULL DEFAULT 0,
	INDEX IDX_recados_horario (idContato, horario DESC),
	INDEX FK_recados_contatos (idContato),
	CONSTRAINT FK_recados_contatos 
		FOREIGN KEY (idContato) REFERENCES contatos (id),
	PRIMARY KEY(id)
);