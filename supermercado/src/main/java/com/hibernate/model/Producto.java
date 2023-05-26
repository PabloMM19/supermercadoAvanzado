package com.hibernate.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="producto")
public class Producto {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="idProducto")
	private int idProducto;
	@Column(name="nombreProducto")
	private String nombreProducto;
	@Column(name="precioProducto")
	private double precioProducto;
	@Column(name="unidadesProducto")
	private int unidadesProducto;
	@Column(name="fotoProducto")
	private String fotoProducto;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCategoria")
	private Categoria categoria;

	public Producto() {
		super();
	}

	
	/**
	 * Crea una nueva instancia de la clase Producto con el nombre, precio, unidades, foto y categoría especificados.
	 * @param nombreProducto El nombre del producto.
	 * @param precioProducto El precio del producto.
	 * @param unidadesProducto El número de unidades del producto.
	 * @param fotoProducto La ruta de la foto del producto.
	 * @param categoria La categoría del producto.
	 */
	public Producto(String nombreProducto, double precioProducto, int unidadesProducto, String fotoProducto,
	                Categoria categoria) {
	    super();
	    this.nombreProducto = nombreProducto;
	    this.precioProducto = precioProducto;
	    this.unidadesProducto = unidadesProducto;
	    this.fotoProducto = fotoProducto;
	    this.categoria = categoria;
	}



	/**
	 * @return the idProducto
	 */
	public int getIdProducto() {
		return idProducto;
	}

	/**
	 * @param idProducto the idProducto to set
	 */
	public void setIdProducto(int idProducto) {
		this.idProducto = idProducto;
	}

	/**
	 * @return the nombreProducto
	 */
	public String getNombreProducto() {
		return nombreProducto;
	}

	/**
	 * @param nombreProducto the nombreProducto to set
	 */
	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
	}

	/**
	 * @return the precioProducto
	 */
	public double getPrecioProducto() {
		return precioProducto;
	}

	/**
	 * @param precioProducto the precioProducto to set
	 */
	public void setPrecioProducto(double precioProducto) {
		this.precioProducto = precioProducto;
	}

	/**
	 * @return the unidadesProducto
	 */
	public int getUnidadesProducto() {
		return unidadesProducto;
	}

	/**
	 * @param unidadesProducto the unidadesProducto to set
	 */
	public void setUnidadesProducto(int unidadesProducto) {
		this.unidadesProducto = unidadesProducto;
	}

	/**
	 * @return the fotoProducto
	 */
	public String getFotoProducto() {
		return fotoProducto;
	}

	/**
	 * @param fotoProducto the fotoProducto to set
	 */
	public void setFotoProducto(String fotoProducto) {
		this.fotoProducto = fotoProducto;
	}

	/**
	 * @return the categoria
	 */
	public Categoria getCategoria() {
		return categoria;
	}

	/**
	 * @param categoria the categoria to set
	 */
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	
	
	
	
}
