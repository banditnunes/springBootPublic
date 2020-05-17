package br.com.alura.curso.springboot.forum.model;

import java.io.Serializable;

public enum StatusTopico implements Serializable {
	
	NAO_RESPONDIDO,
	NAO_SOLUCIONADO,
	SOLUCIONADO,
	FECHADO;

}
