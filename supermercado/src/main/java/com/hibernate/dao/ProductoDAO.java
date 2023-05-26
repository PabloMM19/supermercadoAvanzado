package com.hibernate.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.hibernate.model.Producto;
import com.hibernate.util.HibernateUtil;

public class ProductoDAO {
	/**
	 * Inserta un nuevo producto en la base de datos.
	 * 
	 * @param p El producto a insertar.
	 */
	public void insertProducto(Producto p) {

		Transaction transaction = null;

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.persist(p);
			transaction.commit();

		} catch (Exception e) {
			if (transaction != null) {

				transaction.rollback();
			}
		}
	}

	/**
	 * Actualiza un producto existente en la base de datos.
	 * 
	 * @param p El producto a actualizar.
	 */
	public void updateProducto(Producto p) {

		Transaction transaction = null;

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.merge(p);
			transaction.commit();

		} catch (Exception e) {
			if (transaction != null) {

				transaction.rollback();
			}
		}
	}

	/**
	 * Elimina un producto de la base de datos.
	 * 
	 * @param codigo El código del producto a eliminar.
	 */
	public void deleteProducto(int codigo) {

		Transaction transaction = null;
		Producto p = null;

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			p = session.get(Producto.class, codigo);
			session.remove(p);
			transaction.commit();

		} catch (Exception e) {
			if (transaction != null) {

				transaction.rollback();
			}
		}
	}

	/**
	 * Obtiene todos los productos de la base de datos.
	 * 
	 * @return Una lista de todos los productos.
	 */
	public List<Producto> selectAllProducto() {

		Transaction transaction = null;
		List<Producto> productos = null;

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			productos = session.createQuery("from Producto", Producto.class).getResultList();
			transaction.commit();

		} catch (Exception e) {
			if (transaction != null) {

				transaction.rollback();
			}
		}
		return productos;
	}

	/**
	 * Obtiene los productos de una categoría específica por su ID de categoría.
	 * 
	 * @param idCategoria El ID de la categoría.
	 * @return Una lista de productos de la categoría especificada.
	 */
	public List<Producto> selectProductosByIdCategoria(int idCategoria) {

		Transaction transaction = null;
		List<Producto> productoDeCategoria = null;

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			Query<Producto> query = session.createQuery("FROM Producto WHERE categoria.idCategoria = :idCategoria",
					Producto.class);
			query.setParameter("idCategoria", idCategoria);
			productoDeCategoria = query.getResultList();

			transaction.commit();

		} catch (Exception e) {
			if (transaction != null) {

				transaction.rollback();
			}
		}
		return productoDeCategoria;
	}

	/**
	 * Obtiene los productos sin stock de la base de datos.
	 * 
	 * @return Una lista de productos sin stock.
	 */
	public List<Producto> selectProductosSinStock() {

		Transaction transaction = null;
		List<Producto> productoSinStock = null;

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			Query<Producto> query = session.createQuery("FROM Producto WHERE unidadesProducto = 0", Producto.class);
			productoSinStock = query.getResultList();

			transaction.commit();

		} catch (Exception e) {
			if (transaction != null) {

				transaction.rollback();
			}
		}
		return productoSinStock;
	}

	/**
	 * Obtiene un producto de la base de datos por su código.
	 * 
	 * @param codigo El código del producto.
	 * @return El producto encontrado, o null si no se encuentra.
	 */
	public Producto selectProductoById(int codigo) {

		Transaction transaction = null;
		Producto p = null;

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			p = session.get(Producto.class, codigo);
			transaction.commit();

		} catch (Exception e) {
			if (transaction != null) {

				transaction.rollback();
			}
		}
		return p;
	}
}
