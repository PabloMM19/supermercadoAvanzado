package com.hibernate.gui;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import com.formdev.flatlaf.IntelliJTheme;
import com.hibernate.dao.CategoriaDAO;
import com.hibernate.dao.ProductoDAO;
import com.hibernate.model.Categoria;
import com.hibernate.model.Producto;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class App {

	private JFrame frmSupermercado;
	private JTextField tfIdProducto;
	private JTextField tfNombreProducto;
	private JTextField tfPrecioProducto;
	private JTextField tfUnidadesProducto;
	private JComboBox cbCategoria;
	private JComboBox cbCategoriaFiltro;
	private JComboBox cbOfertas;
	private String rutaFoto;
	private JLabel lblEligeFoto;
	private JButton btnEliminarProducto;
	private JButton btnActualizarProducto;
	private JButton btnInsertarProducto;
	private JLabel lblCategoria;
	private JLabel lblUnidades;
	private JLabel lblPrecio;
	private JLabel lblNombreProducto;
	private JLabel lblIdProducto;
	private JScrollPane scrollPane;
	private JTable tableProducto;
	private JLabel lblHomeSupermercado;
	private JLabel lblFotoProducto;
	private JMenuBar menuBar;
	private JButton btnSubirFoto;

	/**
	 * Obtiene la extensión de un archivo.
	 * 
	 * @param file El archivo del que se desea obtener la extensión.
	 * @return La extensión del archivo, en formato de texto.
	 */

	public String getFileExtension(File file) {
		String extension = "";
		String fileName = file.getName();
		int dotIndex = fileName.lastIndexOf('.');
		if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
			extension = fileName.substring(dotIndex + 1).toLowerCase();
		}
		return extension;
	}

	/**
	 * Método utilizado para refrescar una tabla con los productos y categorías
	 * especificados.
	 *
	 * @param modelProducto El modelo de tabla utilizado para mostrar los productos.
	 * @param productoDAO   El objeto ProductoDAO utilizado para acceder a la base
	 *                      de datos de productos.
	 * @param categoriaDAO  El objeto CategoriaDAO utilizado para acceder a la base
	 *                      de datos de categorías.
	 */
	public void refrescarTabla(DefaultTableModel modelProducto, ProductoDAO productoDAO, CategoriaDAO categoriaDAO) {
		List<Producto> productos = null; // Lista de productos obtenidos de la base de datos
		List<Categoria> categorias = null; // Lista de categorías obtenidas de la base de datos
		int idCategoria; // Identificador de la categoría seleccionada

		// Determinar el id de la categoría seleccionada en el JComboBox
		// cbCategoriaFiltro
		switch (cbCategoriaFiltro.getSelectedIndex()) {
		case 0:
			idCategoria = 1000;
			break;
		case 1:
			idCategoria = 1001;
			break;
		case 2:
			idCategoria = 1;
			break;
		case 3:
			idCategoria = 2;
			break;
		// ... continuar con los demás casos hasta el caso 11
		default:
			idCategoria = 10;
			break;
		}

		modelProducto.setRowCount(0); // Limpiar el contenido actual del modelo de tabla

		// Obtener la lista de productos y categorías según el id de la categoría
		// seleccionada
		if (idCategoria == 1000) {
			productos = productoDAO.selectAllProducto();
		} else if (idCategoria == 1001) {
			productos = productoDAO.selectProductosSinStock();
		} else {
			productos = productoDAO.selectProductosByIdCategoria(idCategoria);
		}
		categorias = categoriaDAO.selectAllCategoria();

		// Iterar sobre la lista de productos para agregarlos al modelo de tabla
		for (Producto p : productos) {
			Object[] row = new Object[5]; // Arreglo de objetos para representar una fila de la tabla

			double precioInicial = p.getPrecioProducto(); // Precio inicial del producto
			double resultadoFinal; // Precio final del producto con descuento aplicado

			row[0] = p.getIdProducto(); // ID del producto
			row[1] = p.getNombreProducto(); // Nombre del producto

			// Determinar el precio final del producto según la opción seleccionada en el
			// JComboBox cbOfertas
			switch (cbOfertas.getSelectedIndex()) {
			case 0:
				resultadoFinal = p.getPrecioProducto();
				break;
			case 1:
				resultadoFinal = (precioInicial - (p.getPrecioProducto() * 25) / 100);
				break;
			case 2:
				resultadoFinal = (precioInicial - (p.getPrecioProducto() * 50) / 100);
				break;
			case 3:
				resultadoFinal = (precioInicial - (p.getPrecioProducto() * 75) / 100);
				break;
			case 4:
				resultadoFinal = (precioInicial - (p.getPrecioProducto() * 99) / 100);
				break;

			default:
				resultadoFinal = p.getPrecioProducto();
				break;
			}

			row[2] = String.format("%.2f", resultadoFinal).replace(",", "."); // Precio final formateado con dos
																				// decimales
			row[3] = p.getUnidadesProducto(); // Unidades disponibles del producto

			// Obtener la categoría correspondiente al producto y asignar el nombre de la
			// categoría a la última columna
			for (Categoria c : categorias) {
				c = categoriaDAO.selectCategoriaById(p.getCategoria().getIdCategoria());
				row[4] = c.getNombreCategoria();
			}
			modelProducto.addRow(row);
		}
	}

	/**
	 * Método utilizado para limpiar los campos de entrada y la selección de una
	 * tabla.
	 */
	public void limpiarCampos() {
		tfIdProducto.setText(""); // Limpiar el campo de texto del ID del producto
		tfNombreProducto.setText(""); // Limpiar el campo de texto del nombre del producto
		tfPrecioProducto.setText(""); // Limpiar el campo de texto del precio del producto
		tfUnidadesProducto.setText(""); // Limpiar el campo de texto de las unidades del producto
		tableProducto.clearSelection(); // Limpiar la selección de la tabla de productos
		lblFotoProducto.setVisible(false); // Oculta la imagen de profucto
		cbCategoria.setSelectedIndex(0); // Fija el indice del desplegable a 0
	}

	/**
	 * Actualiza la apariencia y el diseño de un JFrame.
	 * 
	 * @param frame El JFrame que se va a actualizar.
	 */
	private static void updateUI(JFrame frame) {
		SwingUtilities.updateComponentTreeUI(frame); // Actualiza la apariencia de los componentes en el JFrame
		frame.validate(); // Valida el diseño de los componentes
		frame.repaint(); // Repinta el JFrame para reflejar los cambios visuales
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IntelliJTheme.setup(App.class.getResourceAsStream("temas/claros/arc-theme-orange.theme.json"));
					App window = new App();
					window.frmSupermercado.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public App() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		/**
		 * Patrones de expresiones regulares utilizados para validar diferentes formatos
		 * de texto.
		 */
		Pattern patDecimal = Pattern.compile("^\\d+(\\.\\d+)?$"); // Patrón para validar números decimales
		Pattern patEntero = Pattern.compile("^\\d{1,4}$"); // Patrón para validar números enteros de hasta 4 dígitos
		Pattern patNombre = Pattern.compile("^[A-Za-z]{2,}(\\s[A-Z?a-z])?(\\s[A-Z?a-z])?(\\s[A-Z?a-z])?$"); // Patrón
																											// para
																											// validar
																											// nombres
																											// con un
																											// mínimo de
																											// 2 letras

		/**
		 * Instancias de las clases Categoria y Producto.
		 */
		Categoria categoria = new Categoria(); // Instancia de la clase Categoria
		Producto producto = new Producto(); // Instancia de la clase Producto

		/**
		 * Instancias de las clases ProductoDAO y CategoriaDAO para acceder a los datos.
		 */
		ProductoDAO productoDAO = new ProductoDAO(); // Instancia de la clase ProductoDAO para acceder a los datos de
														// los productos
		CategoriaDAO categoriaDAO = new CategoriaDAO(); // Instancia de la clase CategoriaDAO para acceder a los datos
														// de las categorías

		/**
		 * Listas para almacenar categorías y productos.
		 */
		List<Categoria> categorias = null; // Lista para almacenar las categorías
		List<Producto> productos = null; // Lista para almacenar los productos

		frmSupermercado = new JFrame();
		frmSupermercado.setBounds(100, 100, 1000, 625);
		frmSupermercado.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSupermercado.getContentPane().setLayout(null);

		menuBar = new JMenuBar();
		frmSupermercado.setJMenuBar(menuBar);

		JMenu mnApariencia = new JMenu("Apariencia");
		menuBar.add(mnApariencia);

		JMenuItem mntmOpcionFP = new JMenuItem("FirePunch");
		mntmOpcionFP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/**
				 * Personalización con un tema de FlatLaf almacenado en los paquetes dentro del
				 * paquete gui.
				 */
				IntelliJTheme.setup(App.class.getResourceAsStream("temas/claros/arc-theme-orange.theme.json"));
				updateUI(frmSupermercado);

				JOptionPane.showMessageDialog(frmSupermercado, "Aspecto cambiado a FirePunch");
			}
		});
		mnApariencia.add(mntmOpcionFP);

		JMenuItem mntmOpcionEM = new JMenuItem("Esmeralda");
		mntmOpcionEM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/**
				 * Personalización con un tema de FlatLaf almacenado en los paquetes dentro del
				 * paquete gui.
				 */
				IntelliJTheme.setup(App.class.getResourceAsStream("temas/colores/Gradianto_Nature_Green.theme.json"));
				updateUI(frmSupermercado);

				JOptionPane.showMessageDialog(frmSupermercado, "Aspecto cambiado a Esmeralda");
			}
		});
		mnApariencia.add(mntmOpcionEM);

		JMenuItem mntmOpcionAZ = new JMenuItem("Azerbaiyán");
		mntmOpcionAZ.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/**
				 * Personalización con un tema de FlatLaf almacenado en los paquetes dentro del
				 * paquete gui.
				 */
				IntelliJTheme.setup(App.class.getResourceAsStream("temas/claros/Github Contrast.theme.json"));
				updateUI(frmSupermercado);

				JOptionPane.showMessageDialog(frmSupermercado, "Aspecto cambiado a Azerbaiyán");
			}
		});
		mnApariencia.add(mntmOpcionAZ);

		JMenuItem mntmOpcionMM = new JMenuItem("Mismagius");
		mntmOpcionMM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/**
				 * Personalización con un tema de FlatLaf almacenado en los paquetes dentro del
				 * paquete gui.
				 */
				IntelliJTheme.setup(App.class.getResourceAsStream("temas/colores/Material Palenight.theme.json"));
				updateUI(frmSupermercado);

				JOptionPane.showMessageDialog(frmSupermercado, "Aspecto cambiado a Mismagius");
			}
		});
		mnApariencia.add(mntmOpcionMM);

		JMenuItem mntmOpcionLM = new JMenuItem("Lemon Milk");
		mntmOpcionLM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/**
				 * Personalización con un tema de FlatLaf almacenado en los paquetes dentro del
				 * paquete gui.
				 */
				IntelliJTheme.setup(App.class.getResourceAsStream("temas/oscuros/Monokai Pro Contrast.theme.json"));
				updateUI(frmSupermercado);

				JOptionPane.showMessageDialog(frmSupermercado, "Aspecto cambiado a Lemon Milk");
			}
		});
		mnApariencia.add(mntmOpcionLM);

		JMenuItem mntmOpcionAM = new JMenuItem("Azumarill");
		mntmOpcionAM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/**
				 * Personalización con un tema de FlatLaf almacenado en los paquetes dentro del
				 * paquete gui.
				 */
				IntelliJTheme.setup(App.class.getResourceAsStream("temas/colores/Moonlight.theme.json"));
				updateUI(frmSupermercado);

				JOptionPane.showMessageDialog(frmSupermercado, "Aspecto cambiado a Azumarill");
			}
		});
		mnApariencia.add(mntmOpcionAM);

		JMenuItem mntmOpcionFR = new JMenuItem("Frambuesa");
		mntmOpcionFR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/**
				 * Personalización con un tema de FlatLaf almacenado en los paquetes dentro del
				 * paquete gui.
				 */
				IntelliJTheme.setup(App.class.getResourceAsStream("temas/colores/Solarized Light.theme.json"));
				updateUI(frmSupermercado);

				JOptionPane.showMessageDialog(null, "Aspecto cambiado a Frambuesa");
			}
		});
		mnApariencia.add(mntmOpcionFR);

		JMenuItem mntmOpcionN2 = new JMenuItem("Naranjito 2");
		mntmOpcionN2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/**
				 * Personalización con un tema de FlatLaf almacenado en los paquetes dentro del
				 * paquete gui.
				 */
				IntelliJTheme.setup(App.class.getResourceAsStream("temas/oscuros/arc_theme_dark_orange.theme.json"));
				updateUI(frmSupermercado);

				JOptionPane.showMessageDialog(frmSupermercado, "Aspecto cambiado a Naranjito 2");
			}
		});
		mnApariencia.add(mntmOpcionN2);

		lblHomeSupermercado = new JLabel("Almacén del supermercado");
		lblHomeSupermercado.setFont(new Font("Dialog", Font.BOLD, 15));
		lblHomeSupermercado.setHorizontalAlignment(SwingConstants.CENTER);
		lblHomeSupermercado.setBounds(380, 28, 223, 15);
		frmSupermercado.getContentPane().add(lblHomeSupermercado);

		DefaultTableModel modelProducto = new DefaultTableModel() {
			public boolean isCellEditable(int fila, int columna) {
				return false; // No permitir la edición de las celdas
			}
		};

		modelProducto.addColumn("ID");
		modelProducto.addColumn("Nombre");
		modelProducto.addColumn("Precio");
		modelProducto.addColumn("Unidades");
		modelProducto.addColumn("Categoria");

		productos = productoDAO.selectAllProducto();
		categorias = categoriaDAO.selectAllCategoria();

		for (Producto p : productos) {
			Object[] row = new Object[5];

			row[0] = p.getIdProducto();
			row[1] = p.getNombreProducto();
			row[2] = p.getPrecioProducto();
			row[3] = p.getUnidadesProducto();
			for (Categoria c : categorias) {
				c = categoriaDAO.selectCategoriaById(p.getCategoria().getIdCategoria());
				row[4] = c.getNombreCategoria();
			}
			modelProducto.addRow(row);
		}

		tableProducto = new JTable(modelProducto);
		tableProducto.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int indiceCategoria = 0;
				int index = tableProducto.getSelectedRow();
				TableModel model = tableProducto.getModel();

				lblFotoProducto.setVisible(true);
				
				tfIdProducto.setText(model.getValueAt(index, 0).toString());
				tfNombreProducto.setText(model.getValueAt(index, 1).toString());
				tfPrecioProducto.setText(model.getValueAt(index, 2).toString());
				tfUnidadesProducto.setText(model.getValueAt(index, 3).toString());

				switch (model.getValueAt(index, 4).toString()) {
				case "Frutas y Verduras":
					indiceCategoria = 0;
					break;
				case "Carnes y Aves":
					indiceCategoria = 1;
					break;
				case "Pescados y Mariscos":
					indiceCategoria = 2;
					break;
				case "Productos lácteos":
					indiceCategoria = 3;
					break;
				case "Panadería y Pastelería":
					indiceCategoria = 4;
					break;
				case "Bebidas":
					indiceCategoria = 5;
					break;
				case "Alimentos enlatados":
					indiceCategoria = 6;
					break;
				case "Cuidado personal":
					indiceCategoria = 7;
					break;
				case "Limpieza del hogar":
					indiceCategoria = 8;
					break;
				case "Higiene personal":
					indiceCategoria = 9;
					break;
				default:
					// indiceCategoria = 10;
				}
				cbCategoria.setSelectedIndex(indiceCategoria);

				Producto p = productoDAO.selectProductoById(Integer.parseInt(model.getValueAt(index, 0).toString()));

				ImageIcon imageIcon = new ImageIcon(p.getFotoProducto());
				Image image = imageIcon.getImage().getScaledInstance(lblFotoProducto.getWidth(),
						lblFotoProducto.getHeight(), Image.SCALE_SMOOTH);
				lblFotoProducto.setIcon(new ImageIcon(image));
				frmSupermercado.getContentPane().add(lblFotoProducto);
			}

		});
		tableProducto.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		TableColumnModel columnModel = tableProducto.getColumnModel();

		DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
		centerRender.setHorizontalAlignment(JLabel.CENTER);

		columnModel.getColumn(0).setPreferredWidth(60);
		columnModel.getColumn(0).setCellRenderer(centerRender);
		columnModel.getColumn(1).setPreferredWidth(200);
		columnModel.getColumn(1).setCellRenderer(centerRender);
		columnModel.getColumn(2).setPreferredWidth(60);
		columnModel.getColumn(2).setCellRenderer(centerRender);
		columnModel.getColumn(3).setPreferredWidth(60);
		columnModel.getColumn(3).setCellRenderer(centerRender);
		columnModel.getColumn(4).setPreferredWidth(140);
		columnModel.getColumn(4).setCellRenderer(centerRender);

		frmSupermercado.getContentPane().add(tableProducto);
		tableProducto.setDefaultEditor(Producto.class, null);

		scrollPane = new JScrollPane(tableProducto);
		scrollPane.setBounds(162, 55, 660, 181);
		frmSupermercado.getContentPane().add(scrollPane);

		lblIdProducto = new JLabel("Id:");
		lblIdProducto.setBounds(203, 249, 70, 15);
		frmSupermercado.getContentPane().add(lblIdProducto);

		lblNombreProducto = new JLabel("Nombre:");
		lblNombreProducto.setBounds(203, 295, 70, 15);
		frmSupermercado.getContentPane().add(lblNombreProducto);

		lblPrecio = new JLabel("Precio:");
		lblPrecio.setBounds(203, 336, 70, 15);
		frmSupermercado.getContentPane().add(lblPrecio);

		lblUnidades = new JLabel("Unidades:");
		lblUnidades.setBounds(203, 380, 96, 15);
		frmSupermercado.getContentPane().add(lblUnidades);

		lblCategoria = new JLabel("Categoría:");
		lblCategoria.setBounds(203, 425, 96, 15);
		frmSupermercado.getContentPane().add(lblCategoria);

		tfIdProducto = new JTextField();
		tfIdProducto.setEditable(false);
		tfIdProducto.setBounds(326, 247, 190, 20);
		frmSupermercado.getContentPane().add(tfIdProducto);
		tfIdProducto.setColumns(10);

		tfNombreProducto = new JTextField();
		tfNombreProducto.setColumns(10);
		tfNombreProducto.setBounds(326, 293, 190, 20);
		frmSupermercado.getContentPane().add(tfNombreProducto);

		tfPrecioProducto = new JTextField();
		tfPrecioProducto.setColumns(10);
		tfPrecioProducto.setBounds(326, 334, 190, 20);
		frmSupermercado.getContentPane().add(tfPrecioProducto);

		tfUnidadesProducto = new JTextField();
		tfUnidadesProducto.setColumns(10);
		tfUnidadesProducto.setBounds(326, 378, 190, 20);
		frmSupermercado.getContentPane().add(tfUnidadesProducto);

		cbOfertas = new JComboBox();
		cbOfertas.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				refrescarTabla(modelProducto, productoDAO, categoriaDAO);
				limpiarCampos();
			}
		});
		cbOfertas.setModel(new DefaultComboBoxModel(new String[] { "Sin oferta", "Oferta del 25%", "Oferta del 50%",
				"Oferta del 75%", "Oferta de liquidación" }));
		cbOfertas.setBounds(671, 25, 151, 24);
		frmSupermercado.getContentPane().add(cbOfertas);

		cbCategoria = new JComboBox();
		cbCategoria.setBounds(326, 420, 190, 24);

		cbCategoriaFiltro = new JComboBox();
		cbCategoriaFiltro.setModel(new DefaultComboBoxModel(new String[] { "Ver todos", "Sin stock" }));
		cbCategoriaFiltro.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				refrescarTabla(modelProducto, productoDAO, categoriaDAO);
				limpiarCampos();
			}
		});
		cbCategoriaFiltro.setBounds(162, 25, 151, 24);

		categorias = categoriaDAO.selectAllCategoria();
		cbCategoria.removeAllItems();

		int numeral = 1;
		for (Categoria c : categorias) {
			cbCategoria.addItem(numeral + ". " + c.getNombreCategoria());
			cbCategoriaFiltro.addItem(numeral + ". " + c.getNombreCategoria());
			numeral++;
		}

		frmSupermercado.getContentPane().add(cbCategoriaFiltro);
		frmSupermercado.getContentPane().add(cbCategoria);

		btnSubirFoto = new JButton("Elige Foto");
		btnSubirFoto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int returnValue = fileChooser.showOpenDialog(null);

				FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "gif");
				fileChooser.setFileFilter(filter);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();

					ImageIcon imageIcon = new ImageIcon(selectedFile.getAbsolutePath());
					Image image = imageIcon.getImage().getScaledInstance(lblFotoProducto.getWidth(),
							lblFotoProducto.getHeight(), Image.SCALE_SMOOTH);
					lblFotoProducto.setIcon(new ImageIcon(image));

					String extension = getFileExtension(selectedFile);

					Random random = new Random();
					String fileName = "image_" + random.nextInt(1000000) + "." + extension;

					// Copy the selected file to the desired location
					try {
						File destination = new File("src/main/resources/" + fileName);
						rutaFoto = "src/main/resources/" + fileName;
						Files.copy(selectedFile.toPath(), destination.toPath());
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		btnSubirFoto.setBounds(326, 461, 190, 23);
		frmSupermercado.getContentPane().add(btnSubirFoto);

		btnInsertarProducto = new JButton("Insertar");
		btnInsertarProducto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				// Validar el formato del precio y las unidades utilizando los patrones
				// definidos
				Matcher matPrecio = patDecimal.matcher(tfPrecioProducto.getText()); // Matcher para validar el formato
																					// del precio
				Matcher matUnidades = patEntero.matcher(tfUnidadesProducto.getText()); // Matcher para validar el
																						// formato de las unidades
				Matcher matNombre = patNombre.matcher(tfNombreProducto.getText());
				// Comprobar si alguno de los campos está vacío
				if (tfNombreProducto.getText().isEmpty() || tfPrecioProducto.getText().isEmpty()
						|| tfUnidadesProducto.getText().isEmpty()) {
					JOptionPane.showMessageDialog(frmSupermercado, "Rellena todos los campos, por favor.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}

				else if (!matNombre.matches()) {
					JOptionPane.showMessageDialog(frmSupermercado,
							"El formato del nombre no es el correcto.\nEj: Sopa de pollo", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
				// Comprobar si el formato del precio no es válido
				else if (!matPrecio.matches()) {
					JOptionPane.showMessageDialog(frmSupermercado,
							"El formato del precio no es el correcto.\nEj: 27.56", "Error", JOptionPane.ERROR_MESSAGE);
				}
				// Comprobar si el precio es inferior o igual a cero
				else if (Double.parseDouble(tfPrecioProducto.getText()) <= 0) {
					JOptionPane.showMessageDialog(frmSupermercado, "El precio no puede ser inferior a 0.\nEj: 27.56",
							"Error", JOptionPane.ERROR_MESSAGE);
				}
				// Comprobar si el formato de las unidades no es válido
				else if (!matUnidades.matches()) {
					JOptionPane.showMessageDialog(frmSupermercado,
							"El formato de las unidades no es el correcto.\nEj: 89", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
				// Comprobar si el número de unidades es menor que cero
				else if (Integer.parseInt(tfUnidadesProducto.getText()) < 0) {
					JOptionPane.showMessageDialog(frmSupermercado, "Mínimo tiene que haber 0 unidades.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
				// Comprobar si el número de unidades es mayor que 9999
				else if (Integer.parseInt(tfUnidadesProducto.getText()) > 9999) {
					JOptionPane.showMessageDialog(frmSupermercado, "No puede haber más de 9999 unidades.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
				// Si todas las validaciones son exitosas, se actualiza el producto
				else {

					Categoria categoria = categoriaDAO.selectCategoriaById(cbCategoria.getSelectedIndex() + 1);
					Producto producto = new Producto(tfNombreProducto.getText(),
							Double.parseDouble(tfPrecioProducto.getText()),
							Integer.parseInt(tfUnidadesProducto.getText()), rutaFoto, categoria);
					productoDAO.insertProducto(producto);
					JOptionPane.showMessageDialog(frmSupermercado, "Producto creado");
					refrescarTabla(modelProducto, productoDAO, categoriaDAO);
					limpiarCampos();
				}
			}
		});
		btnInsertarProducto.setBounds(265, 515, 117, 25);
		frmSupermercado.getContentPane().add(btnInsertarProducto);

		btnActualizarProducto = new JButton("Actualizar");
		btnActualizarProducto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Validar el formato del precio y las unidades utilizando los patrones
				// definidos
				Matcher matPrecio = patDecimal.matcher(tfPrecioProducto.getText()); // Matcher para validar el formato
																					// del precio
				Matcher matUnidades = patEntero.matcher(tfUnidadesProducto.getText()); // Matcher para validar el
																						// formato de las unidades

				// Comprobar si alguno de los campos está vacío
				if (tfNombreProducto.getText().isEmpty() || tfPrecioProducto.getText().isEmpty()
						|| tfUnidadesProducto.getText().isEmpty()) {
					JOptionPane.showMessageDialog(frmSupermercado, "Rellena todos los campos, por favor.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
				// Comprobar si el formato del precio no es válido
				else if (!matPrecio.matches()) {
					JOptionPane.showMessageDialog(frmSupermercado,
							"El formato del precio no es el correcto.\nEj: 27.56", "Error", JOptionPane.ERROR_MESSAGE);
				}
				// Comprobar si el precio es inferior o igual a cero
				else if (Double.parseDouble(tfPrecioProducto.getText()) <= 0) {
					JOptionPane.showMessageDialog(frmSupermercado, "El precio no puede ser inferior a 0.\nEj: 27.56",
							"Error", JOptionPane.ERROR_MESSAGE);
				}
				// Comprobar si el formato de las unidades no es válido
				else if (!matUnidades.matches()) {
					JOptionPane.showMessageDialog(frmSupermercado,
							"El formato de las unidades no es el correcto.\nEj: 89", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
				// Comprobar si el número de unidades es menor que cero
				else if (Integer.parseInt(tfUnidadesProducto.getText()) < 0) {
					JOptionPane.showMessageDialog(frmSupermercado, "Mínimo tiene que haber 0 unidades.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
				// Comprobar si el número de unidades es mayor que 9999
				else if (Integer.parseInt(tfUnidadesProducto.getText()) > 9999) {
					JOptionPane.showMessageDialog(frmSupermercado, "No puede haber más de 9999 unidades.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
				// Si todas las validaciones son exitosas, se actualiza el producto
				else {
					Categoria categoria = categoriaDAO.selectCategoriaById(cbCategoria.getSelectedIndex() + 1);
					Producto producto = productoDAO.selectProductoById(Integer.parseInt(tfIdProducto.getText()));
					producto.setNombreProducto(tfNombreProducto.getText());
					producto.setPrecioProducto(Double.parseDouble(tfPrecioProducto.getText()));
					producto.setUnidadesProducto(Integer.parseInt(tfUnidadesProducto.getText()));
					if (producto.getFotoProducto()==null) {
						producto.setFotoProducto(rutaFoto);
					} else {
						producto.setFotoProducto(producto.getFotoProducto());
					}
					producto.setFotoProducto(rutaFoto);
					producto.setCategoria(categoria);
					productoDAO.updateProducto(producto);
					JOptionPane.showMessageDialog(frmSupermercado, "Producto actualizado");
					refrescarTabla(modelProducto, productoDAO, categoriaDAO);
					limpiarCampos();
				}
			}
		});
		btnActualizarProducto.setBounds(433, 515, 117, 25);
		frmSupermercado.getContentPane().add(btnActualizarProducto);

		btnEliminarProducto = new JButton("Eliminar");
		btnEliminarProducto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (tfIdProducto.getText().isEmpty()) {
					JOptionPane.showMessageDialog(frmSupermercado, "Selecciona un producto a borrar, por favor.",
							"Error", JOptionPane.ERROR_MESSAGE);
				} else {
					productoDAO.deleteProducto(Integer.parseInt(tfIdProducto.getText())); // Eliminar el producto de la
																							// base de datos utilizando
																							// su ID
					JOptionPane.showMessageDialog(frmSupermercado, "Producto borrado"); // Mostrar mensaje de éxito
					refrescarTabla(modelProducto, productoDAO, categoriaDAO); // Actualizar la tabla de productos
					limpiarCampos(); // Limpiar los campos
				}
			}
		});
		btnEliminarProducto.setBounds(595, 515, 117, 25);
		frmSupermercado.getContentPane().add(btnEliminarProducto);

		lblEligeFoto = new JLabel("Foto:");
		lblEligeFoto.setBounds(203, 465, 96, 15);
		frmSupermercado.getContentPane().add(lblEligeFoto);

		lblFotoProducto = new JLabel("");
		lblFotoProducto.setBounds(565, 252, 200, 200);
		frmSupermercado.getContentPane().add(lblFotoProducto);

	}
}
