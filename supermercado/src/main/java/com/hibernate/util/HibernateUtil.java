package com.hibernate.util;

import java.util.Properties;

import org.hibernate.*;
import org.hibernate.boot.registry.*;
import org.hibernate.cfg.*;
import org.hibernate.service.*;

import com.hibernate.model.Categoria;
import com.hibernate.model.Producto;

/**
 * Esta clase proporciona la configuración y la instancia de la sesión de
 * Hibernate para interactuar con la base de datos.
 */
public class HibernateUtil {
	private static SessionFactory sessionFactory;

	/**
	 * Obtiene la instancia de la fábrica de sesiones de Hibernate.
	 * 
	 * @return La fábrica de sesiones de Hibernate.
	 */
	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			try {
				Configuration configuration = new Configuration();
				Properties settings = new Properties();
				settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
				settings.put(Environment.URL, "jdbc:mysql://127.0.0.1:3307/almacenSupermercado?useSSL=false");
				settings.put(Environment.USER, "alumno");
				settings.put(Environment.PASS, "alumno");
				settings.put(Environment.SHOW_SQL, "false");
				settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
				settings.put(Environment.HBM2DDL_AUTO, "update");

				configuration.setProperties(settings);
				configuration.addAnnotatedClass(Producto.class);
				configuration.addAnnotatedClass(Categoria.class);
				ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
						.applySettings(configuration.getProperties()).build();
				sessionFactory = configuration.buildSessionFactory(serviceRegistry);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sessionFactory;
	}
}