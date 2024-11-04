package org.iesvdm.tienda;

import org.iesvdm.tienda.modelo.Fabricante;
import org.iesvdm.tienda.modelo.Producto;
import org.iesvdm.tienda.repository.FabricanteRepository;
import org.iesvdm.tienda.repository.ProductoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Comparator.*;
import static java.util.stream.Collectors.toList;


@SpringBootTest
class TiendaApplicationTests {

	@Autowired
	FabricanteRepository fabRepo;
	
	@Autowired
	ProductoRepository prodRepo;

	@Test
	void testAllFabricante() {
		var listFabs = fabRepo.findAll();
		
		listFabs.forEach(f -> {
			System.out.println(">>"+f+ ":");
			f.getProductos().forEach(System.out::println);
		});
	}
	
	@Test
	void testAllProducto() {
		var listProds = prodRepo.findAll();

		listProds.forEach( p -> {
			System.out.println(">>"+p+":"+"\nProductos mismo fabricante "+ p.getFabricante());
			p.getFabricante().getProductos().forEach(pF -> System.out.println(">>>>"+pF));
		});
				
	}

	/**
	 * 1. Lista los nombres y los precios de todos los productos de la tabla producto
	 */
	@Test
	void test1() {
		var listProds = prodRepo.findAll();
		listProds.forEach( p -> System.out.println("Nombre: "+p.getNombre()+" Precio: "+p.getPrecio()));
	}

	/**
	 * 2. Devuelve una lista de Producto completa con el precio de euros convertido a dólares.
	 */
	@Test
	void test2() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.map(p->{p.setPrecio(p.getPrecio()*1.1);return p;})
				.toList();
		result.forEach(p->System.out.println(p.getPrecio()));
		Assertions.assertEquals(result.getFirst().getPrecio(),95.68900000000001);
	}
	
	/**
	 * 3. Lista los nombres y los precios de todos los productos, convirtiendo los nombres a mayúscula.
	 */
	@Test
	void test3() {
		var listProds = prodRepo.findAll();
		record nomPrecios (String nombre, Double precio){};
		var copia = listProds.stream().map(p->new nomPrecios(p.getNombre().toUpperCase(),p.getPrecio())).toList();
		System.out.println(copia);
	}
	
	/**
	 * 4. Lista el nombre de todos los fabricantes y a continuación en mayúsculas los dos primeros caracteres del nombre del fabricante.
	 */
	@Test
	void test4() {
		var listFabs = fabRepo.findAll();
		record Tupla (String nombre, String iniciales){};
		var result = listFabs.stream()
				.map(p -> new Tupla(p.getNombre(),p.getNombre().substring(0, 2).toUpperCase()))
				.toList();
		result.forEach(System.out::println);
	}

	/**
	 * 5. Lista el código de los fabricantes que tienen productos.
	 */
	@Test
	void test5() {
		var listFabs = fabRepo.findAll();
		var result = listFabs.stream()
				.filter(p->!p.getProductos().isEmpty())
				.toList();
		result.forEach(f->System.out.println(f.getCodigo()));
		Assertions.assertEquals(result.size(),7);
	}
	
	/**
	 * 6. Lista los nombres de los fabricantes ordenados de forma descendente.
	 */
	@Test
	void test6() {
		var listFabs = fabRepo.findAll();
		var nombres=listFabs.stream()
				.sorted(comparing(Fabricante::getNombre,reverseOrder()))
				.toList();
		nombres.forEach(System.out::println);
		Assertions.assertEquals(nombres.getFirst().getNombre(),"Xiaomi");
	}
	
	/**
	 * 7. Lista los nombres de los productos ordenados en primer lugar por el nombre de forma ascendente y en segundo lugar por el precio de forma descendente.
	 */
	@Test
	void test7() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				//Hago este map para setear los nombre a minuscula, porque al hacer un sort cuando hay un nombre con una minuscula java lo detecta como
				//menor que una mayuscula cuando MySQL no lo hace por lo que NO corresponden los resultados.
				.map(producto -> {
					producto.setNombre(producto.getNombre().toLowerCase());
					return producto;
				})
				.sorted(comparing(Producto::getNombre)
						.thenComparing(Producto::getPrecio,reverseOrder()))
				.toList();
		result.forEach(p->System.out.println("Nombre: "+p.getNombre()+" - Precio: "+p.getPrecio()));
		Assertions.assertEquals(result.getFirst().getNombre(),"disco duro sata3 1tb");
	}
	
	/**
	 * 8. Devuelve una lista con los 5 primeros fabricantes.
	 */
	@Test
	void test8() {
		var listFabs = fabRepo.findAll();
		var result = listFabs.stream()
				.limit(5)
				.toList();
		result.forEach(System.out::println);
		Assertions.assertEquals(result.size(),5);
	}
	
	/**
	 * 9.Devuelve una lista con 2 fabricantes a partir del cuarto fabricante. El cuarto fabricante también se debe incluir en la respuesta.
	 */
	@Test
	void test9() {
		var listFabs = fabRepo.findAll();
		var result = listFabs.stream()
				.skip(3)
				.limit(2)
				.toList();
		result.forEach(System.out::println);
		Assertions.assertEquals(result.size(),2);
		Assertions.assertEquals(result.get(0).getNombre(),"Samsung");
	}
	
	/**
	 * 10. Lista el nombre y el precio del producto más barato
	 */
	@Test
	void test10() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.sorted(comparing(Producto::getPrecio))
				.limit(1)
				.toList();
		result.forEach(p->System.out.println("Nombre: "+p.getNombre()+" - Precio: "+p.getPrecio()));
		Assertions.assertEquals(result.getFirst().getPrecio(),59.99);
	}
	
	/**
	 * 11. Lista el nombre y el precio del producto más caro
	 */
	@Test
	void test11() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.sorted(comparing(Producto::getPrecio,reverseOrder()))
				.limit(1)
				.toList();

		listProds.stream()
				.max(comparing(p->p.getPrecio()))
				.ifPresentOrElse(p->System.out.println(p.getNombre()+" "+p.getPrecio()), () -> System.out.println("error"));

		result.forEach(p->System.out.println("Nombre: "+p.getNombre()+" - Precio: "+p.getPrecio()));
		Assertions.assertEquals(result.getFirst().getPrecio(),755);
	}
	
	/**
	 * 12. Lista el nombre de todos los productos del fabricante cuyo código de fabricante es igual a 2.
	 * 
	 */
	@Test
	void test12() {
		var listProds = prodRepo.findAll();
		var result= listProds.stream()
				.filter(p->p.getFabricante().getCodigo()==2)
				.toList();
		result.forEach(p->System.out.println(p.getNombre()));
		result.forEach(r->Assertions.assertEquals(r.getFabricante().getCodigo(),2));
	}
	
	/**
	 * 13. Lista el nombre de los productos que tienen un precio menor o igual a 120€.
	 */
	@Test
	void test13() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.filter(p->p.getPrecio()<=120)
				.toList();
		result.forEach(p->System.out.println(p.getNombre()));
		result.forEach(r->Assertions.assertTrue(r.getPrecio()<=120));
	}
	
	/**
	 * 14. Lista los productos que tienen un precio mayor o igual a 400€.
	 */
	@Test
	void test14() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.filter(p->p.getPrecio() >= 400)
				.toList();
		result.forEach(System.out::println);
		result.forEach(p->Assertions.assertTrue(p.getPrecio()>=400));
	}
	
	/**
	 * 15. Lista todos los productos que tengan un precio entre 80€ y 300€. 
	 */
	@Test
	void test15() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.filter(p->p.getPrecio() >= 80&&p.getPrecio()<=300)
				.toList();
		result.forEach(System.out::println);
		result.forEach(p->Assertions.assertTrue(p.getPrecio()>=80&&p.getPrecio()<=300));
	}
	
	/**
	 * 16. Lista todos los productos que tengan un precio mayor que 200€ y que el código de fabricante sea igual a 6.
	 */
	@Test
	void test16() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.filter(p->p.getPrecio()>200&&p.getCodigo()==5)
				.toList();
		result.forEach(System.out::println);
		result.forEach(p->Assertions.assertTrue(p.getPrecio()>200&&p.getCodigo()==5));
	}
	
	/**
	 * 17. Lista todos los productos donde el código de fabricante sea 1, 3 o 5 utilizando un Set de codigos de fabricantes para filtrar.
	 */
	@Test
	void test17() {
		var listProds = prodRepo.findAll();
		Set<Integer> codFabs = Set.of(1,2,5);
		var result = listProds.stream()
				.filter(p -> codFabs.contains(p.getFabricante().getCodigo()))
				.toList();

		result.forEach(System.out::println);
		result.forEach(p->Assertions.assertTrue(codFabs.contains(p.getFabricante().getCodigo())));

	}
	
	/**
	 * 18. Lista el nombre y el precio de los productos en céntimos.
	 */
	@Test
	void test18() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.map(p -> {p.setPrecio(p.getPrecio()*100);return p;})
				.toList();
		result.forEach(p->System.out.println("Nombre: "+p.getNombre()+" - Precio: "+p.getPrecio()));
		Assertions.assertEquals(result.getFirst().getPrecio(), listProds.getFirst().getPrecio());
	}
	
	
	/**
	 * 19. Lista los nombres de los fabricantes cuyo nombre empiece por la letra S
	 */
	@Test
	void test19() {
		var listFabs = fabRepo.findAll();
		var result = listFabs.stream()
				.filter(t-> t.getNombre().substring(0,1).equalsIgnoreCase("S"))
				.toList();
		result.forEach(t->System.out.println(t.getNombre()));
	}
	
	/**
	 * 20. Devuelve una lista con los productos que contienen la cadena Portátil en el nombre.
	 */
	@Test
	void test20() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.filter(p->p.getNombre().contains("Port[a|á]til"))
				.toList();
		result.forEach(System.out::println);
	}
	
	/**
	 * 21. Devuelve una lista con el nombre de todos los productos que contienen la cadena Monitor en el nombre y tienen un precio inferior a 215 €.
	 */
	@Test
	void test21() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.filter(p->p.getNombre().contains("Monitor") && p.getPrecio()<215)
				.toList();
		result.forEach(System.out::println);
		result.forEach(p->Assertions.assertTrue(p.getPrecio()<215&&p.getNombre().contains("Monitor")));
	}
	
	/**
	 * 22. Lista el nombre y el precio de todos los productos que tengan un precio mayor o igual a 180€. 
	 * Ordene el resultado en primer lugar por el precio (en orden descendente) y en segundo lugar por el nombre (en orden ascendente).
	 */
	@Test
	void test22() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.filter(p->p.getPrecio()>=180)
				.sorted(comparing(Producto::getPrecio,reverseOrder())
						.thenComparing(Producto::getNombre))
				.toList();
		result.forEach(p->System.out.println("Nombre: "+p.getNombre()+" - Precio: "+p.getPrecio()));
		result.forEach(p->Assertions.assertTrue(p.getPrecio()>=180));
	}

	/**
	 * 23. Devuelve una lista con el nombre del producto, precio y nombre de fabricante de todos los productos de la base de datos.
	 * Ordene el resultado por el nombre del fabricante, por orden alfabético.
	 */
	@Test
	void test23() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.sorted(comparing(t->t.getFabricante().getNombre()))
				.map(f->f.getNombre() +" - "+ f.getPrecio() +" - "+ f.getFabricante().getNombre())
				.toList();
		result.forEach(System.out::println);
		Assertions.assertEquals(result.getFirst(), "Monitor 24 LED Full HD - 202.0 - Asus");
	}

	/**
	 * 24. Devuelve el nombre del producto, su precio y el nombre de su fabricante, del producto más caro.
	 */
	@Test
	void test24() {
		var listProds = prodRepo.findAll();
		record tupla1(String nombre, Double precio,String nombreFab){};
		var result = listProds.stream()
				.max(comparing(p->p.getPrecio()))
				.map(t->new tupla1(t.getNombre(),t.getPrecio(),t.getFabricante().getNombre()));
		System.out.println(result.orElse(null));
	}
	
	/**
	 * 25. Devuelve una lista de todos los productos del fabricante Crucial que tengan un precio mayor que 200€.
	 */
	@Test
	void test25() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.filter(p-> p.getFabricante().getNombre().equalsIgnoreCase("crucial")&&p.getPrecio()>200)
				.toList();
		result.forEach(System.out::println);
		result.forEach(p->Assertions.assertTrue(p.getPrecio()>200));
	}
	
	/**
	 * 26. Devuelve un listado con todos los productos de los fabricantes Asus, Hewlett-Packard y Seagate
	 */
	@Test
	void test26() {
		var listProds = prodRepo.findAll();
		Set<String> fabs = Set.of("Asus","Hewlett-Packard","Seagate");
		var result = listProds.stream()
				.filter(p-> fabs.contains(p.getFabricante().getNombre()))
				.toList();
		result.forEach(System.out::println);
		Assertions.assertEquals(result.size(), 5);
	}
	
	/**
	 * 27. Devuelve un listado con el nombre de producto, precio y nombre de fabricante, de todos los productos que tengan un precio mayor o igual a 180€. 
	 * Ordene el resultado en primer lugar por el precio (en orden descendente) y en segundo lugar por el nombre.
	 * El listado debe mostrarse en formato tabla. Para ello, procesa las longitudes máximas de los diferentes campos a presentar y compensa mediante la inclusión de espacios en blanco.
	 * La salida debe quedar tabulada como sigue:

Producto                Precio             Fabricante
-----------------------------------------------------
GeForce GTX 1080 Xtreme|611.5500000000001 |Crucial
Portátil Yoga 520      |452.79            |Lenovo
Portátil Ideapd 320    |359.64000000000004|Lenovo
Monitor 27 LED Full HD |199.25190000000003|Asus

	 */		
	@Test
	void test27() {
		var listProds = prodRepo.findAll();
        var result = listProds.stream()
                .filter(p->p.getPrecio()>=180)
                .sorted(comparing(Producto::getPrecio,reverseOrder())
						.thenComparing(Producto::getNombre))
                .toList();

		//Con los siguientes streams obtenemos la longitud máxima de cada campo
        int longitudMaxNombreProd = result.stream()
				//Map para convertir y quedaronos unicamente con la longitud
				.map(p -> p.getNombre().length())
				//Sacamos el maximo
				.max(Integer::compare)
				//En caso de que el Optional esté vacio devolvemos 0
				.orElse(0);
		int longitudMaxPrecio = result.stream()
				.map(p -> String.valueOf(p.getPrecio()).length())
				.max(Integer::compare)
				.orElse(0);
		int longitudMaxNombreFab = result.stream()
				.map(p -> p.getFabricante().getNombre().length())
				.max(Integer::compare)
				.orElse(0);

		//Imprimimos la cabecera
		//el %- seguido de un número indica el número de caracteres que se van a imprimir
		// y la s | %s indica que se va a imprimir un string y luego una barra vertical
		System.out.printf("%-" + longitudMaxNombreProd + "s | %-" + longitudMaxPrecio + "s | %s%n", "Producto", "Precio", "Fabricante");
		//El +6 que se pone al final es para compensar los caracteres de las barras verticales y los espacios en blanco
		System.out.println("-".repeat(longitudMaxNombreProd + longitudMaxPrecio + longitudMaxNombreFab + 6));

		//Imprimimos cada producto en la tabla formateada
		result.forEach(p -> System.out.printf("%-" + longitudMaxNombreProd + "s | %-" + longitudMaxPrecio + "s | %s%n",
				p.getNombre(), p.getPrecio(), p.getFabricante().getNombre()));

	}
	
	/**
	 * 28. Devuelve un listado de los nombres fabricantes que existen en la base de datos, junto con los nombres productos que tiene cada uno de ellos. 
	 * El listado deberá mostrar también aquellos fabricantes que no tienen productos asociados. 
	 * SÓLO SE PUEDEN UTILIZAR STREAM, NO PUEDE HABER BUCLES
	 * La salida debe queda como sigue:
Fabricante: Asus

            	Productos:
            	Monitor 27 LED Full HD
            	Monitor 24 LED Full HD

Fabricante: Lenovo

            	Productos:
            	Portátil Ideapd 320
            	Portátil Yoga 520SELECT f.nombre AS Fabricante,
       GROUP_CONCAT(p.nombre SEPARATOR '\n') AS Productos
FROM Fabricante f
LEFT JOIN Producto p ON f.codigo = p.codigo_fabricante
GROUP BY f.nombre
ORDER BY f.nombre;SELECT f.nombre AS Fabricante,
       GROUP_CONCAT(p.nombre SEPARATOR '\n') AS Productos
FROM Fabricante f
LEFT JOIN Producto p ON f.codigo = p.codigo_fabricante
GROUP BY f.nombre
ORDER BY f.nombre;

Fabricante: Hewlett-Packard

            	Productos:
            	Impresora HP Deskjet 3720
            	Impresora HP Laserjet Pro M26nw

Fabricante: Samsung

            	Productos:
            	Disco SSD 1 TB

Fabricante: Seagate

            	Productos:
            	Disco duro SATA3 1TB

Fabricante: Crucial

            	Productos:
            	GeForce GTX 1080 Xtreme
            	Memoria RAM DDR4 8GB

Fabricante: Gigabyte

            	Productos:
            	GeForce GTX 1050Ti

Fabricante: Huawei

            	Productos:


Fabricante: Xiaomi

            	Productos:

	 */
	@Test
	void test28() {
		var listFabs = fabRepo.findAll();
		//Guardamos los datos que queremos como tabla en un solo String, sin bucles
		String result = listFabs.stream()
				.map(f -> {
					//Con el operador ternario comprobamos si el fabricante tiene productos o no
					String prods = f.getProductos().isEmpty() ? "\tProductos:\n" : f.getProductos().stream()
							//Si tiene productos los mapeamos a un string con el nombre del producto
							.map( p -> "\t\t"+p.getNombre())
							//Une todas las cadeas de productos en una sola separados por \n (saltos de linea)
							.collect(Collectors.joining("\n", "\tProductos:\n", ""));
					//Devolvemos la cadena con el nombre del fabricante y los productos
					return "Fabricante: "+f.getNombre()+"\n"+prods;
				})
				// Une todas las cadenas de fabricantes en una sola cadena, separadas por dos saltos de línea
				.collect(Collectors.joining("\n\n"));
		System.out.println(result);

	}
	
	/**
	 * 29. Devuelve un listado donde sólo aparezcan aquellos fabricantes que no tienen ningún producto asociado.
	 */
	@Test
	void test29() {
		var listFabs = fabRepo.findAll();
		var result = listFabs.stream()
				.filter(f->f.getProductos().isEmpty())
				.toList();
		result.forEach(p->Assertions.assertTrue(p.getProductos().isEmpty()));
		result.forEach(System.out::println);
	}
	
	/**
	 * 30. Calcula el número total de productos que hay en la tabla productos. Utiliza la api de stream.
	 */
	@Test
	void test30() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.count();
		Assertions.assertEquals(result, 11);
	}

	
	/**
	 * 31. Calcula el número de fabricantes con productos, utilizando un stream de Productos.
	 */
	@Test
	void test31() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.map(p->p.getFabricante())
				.distinct()
				.count();
		Assertions.assertEquals(result, 7);
	}

	/**
	 * 32. Calcula la media del precio de todos los productos
	 */
	@Test
	void test32() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.mapToDouble(Producto::getPrecio)
				.average()
				.orElse(0);
		Assertions.assertEquals(result, 271.7236363636364);
	}

	/**
	 * 33. Calcula el precio más barato de todos los productos. No se puede utilizar ordenación de stream.
	 */
	@Test
	void test33() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.mapToDouble(Producto::getPrecio)
				.min()
				.orElse(0);
		Assertions.assertEquals(result, 59.99);
	}
	
	/**
	 * 34. Calcula la suma de los precios de todos los productos.
	 */
	@Test
	void test34() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.mapToDouble(Producto::getPrecio)
				.sum();
		Assertions.assertEquals(result,2988.96);
	}
	
	/**
	 * 35. Calcula el número de productos que tiene el fabricante Asus.
	 */
	@Test
	void test35() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.filter(p->p.getFabricante().getNombre().equalsIgnoreCase("asus"))
				.count();
		Assertions.assertEquals(result,2);
	}
	
	/**
	 * 36. Calcula la media del precio de todos los productos del fabricante Asus.
	 */
	@Test
	void test36() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.filter(p->p.getFabricante().getNombre().equalsIgnoreCase("asus"))
				.mapToDouble(Producto::getPrecio)
				.average()
				.orElse(0);
		Assertions.assertEquals(result,223.995);
	}
	
	
	/**
	 * 37. Muestra el precio máximo, precio mínimo, precio medio y el número total de productos que tiene el fabricante Crucial. 
	 *  Realízalo en 1 solo stream principal. Utiliza reduce con Double[] como "acumulador".
	 */
	@Test
	void test37() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.filter(p->p.getFabricante().getNombre().equalsIgnoreCase("crucial"))
				.map(p -> new Double[]{p.getPrecio(),p.getPrecio(),p.getPrecio(),1.0})
				.reduce((d, d2) -> new Double[]{
						Math.min(d[0],d2[0]),
						Math.max(d[1],d2[1]),
						d[2]+d2[2],
						d[3]+d2[3]})
				.orElse(new Double[]{});
		Double media = result[3]>0 ? result[2]/result[3] : 0.0;
		System.out.println("Minimo: "+result[0]);
		System.out.println("Maximo: "+result[1]);
		System.out.println("Media: "+media);
		System.out.println("Total: "+result[3]);
	}
	
	/**
	 * 38. Muestra el número total de productos que tiene cada uno de los fabricantes. 
	 * El listado también debe incluir los fabricantes que no tienen ningún producto. 
	 * El resultado mostrará dos columnas, una con el nombre del fabricante y otra con el número de productos que tiene. 
	 * Ordene el resultado descendentemente por el número de productos. Utiliza String.format para la alineación de los nombres y las cantidades.
	 * La salida debe queda como sigue:
	 
     Fabricante     #Productos
-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
           Asus              2
         Lenovo              2
Hewlett-Packard              2
        Samsung              1
        Seagate              1
        Crucial              2
       Gigabyte              1
         Huawei              0
         Xiaomi              0

	 */
	@Test
	void test38() {
		var listFabs = fabRepo.findAll();
		var result = listFabs.stream()
				.map(f -> new Object() {
					String nombre = f.getNombre();
					int numProductos = f.getProductos().size();
				})
				.sorted((a, b) -> Integer.compare(b.numProductos, a.numProductos))
				.map(f -> String.format("%-20s %d", f.nombre, f.numProductos))
				.collect(Collectors.joining("\n"));

		System.out.println("Fabricante           #Productos");
		//Este .repeat es para que el caracter se reipa y no escribirlo a mano
		System.out.println("-".repeat(40));
		System.out.println(result);

	}
	
	/**
	 * 39. Muestra el precio máximo, precio mínimo y precio medio de los productos de cada uno de los fabricantes. 
	 * El resultado mostrará el nombre del fabricante junto con los datos que se solicitan. Realízalo en 1 solo stream principal. Utiliza reduce con Double[] como "acumulador".
	 * Deben aparecer los fabricantes que no tienen productos.
	 */
	@Test
	void test39() {
		var listFabs = fabRepo.findAll();

		var result = listFabs.stream()
				.map(f -> {
					Double[] stats = f.getProductos().stream()
							.map(Producto::getPrecio)
							.reduce(new Double[]{Double.MAX_VALUE, 0.0, 0.0, 0.0}, (acc, precio) -> {
								acc[0] = Math.min(acc[0], precio); // Precio mínimo
								acc[1] = Math.max(acc[1], precio); // Precio máximo
								acc[2] += precio; // Suma de precios
								acc[3]++; // Conteo de productos
								return acc;
							}, (acc1, acc2) -> acc1);
					double avg = stats[3] > 0 ? stats[2] / stats[3] : 0.0; // Calcular promedio
					if (stats[3] == 0) { stats[0] = 0.0; stats[1] = 0.0; }
					return String.format("%-20s Min: %-10.2f Max: %-10.2f Avg: %-10.2f", f.getNombre(), stats[0], stats[1], avg);
				})
				.collect(Collectors.joining("\n"));

		System.out.println(result);
	}
	
	/**
	 * 40. Muestra el precio máximo, precio mínimo, precio medio y el número total de productos de los fabricantes que tienen un precio medio superior a 200€. 
	 * No es necesario mostrar el nombre del fabricante, con el código del fabricante es suficiente.
	 */
	@Test
	void test40() {
		var listFabs = fabRepo.findAll();
		var result = listFabs.stream()
				.filter(f->f.getProductos().stream()
						.mapToDouble(Producto::getPrecio)
						.average()
						.orElse(0)>=200)
				.map(f-> new Object(){
						int codFab=f.getCodigo();
						double max=f.getProductos().stream()
								.mapToDouble(Producto::getPrecio)
								.max()
								.orElse(0);
						double min=f.getProductos().stream()
								.mapToDouble(Producto::getPrecio)
								.min()
								.orElse(0);
						double media=f.getProductos().stream()
								.mapToDouble(Producto::getPrecio)
								.average()
								.orElse(0);
						long total = f.getProductos().size();
				})
				.collect(Collectors.toList());
		result.forEach(r->System.out.println("Codigo Fabricante: "+r.codFab+" Max: "+r.max+" Min: "+r.min+" Media: "+r.media+" Total: "+r.total));

	}
	
	/**
	 * 41. Devuelve un listado con los nombres de los fabricantes que tienen 2 o más productos.
	 */
	@Test
	void test41() {
		var listFabs = fabRepo.findAll();
		var result = listFabs.stream()
				.filter(f->f.getProductos().size()>=2)
				.map(Fabricante::getNombre)
				.toList();
		result.forEach(System.out::println);

	}
	
	/**
	 * 42. Devuelve un listado con los nombres de los fabricantes y el número de productos que tiene cada uno con un precio superior o igual a 220 €. 
	 * Ordenado de mayor a menor número de productos.
	 */
	@Test
	void test42() {
		var listFabs = fabRepo.findAll();
		var result = listFabs.stream()
				.map(p -> new Object() {
					String nombreFab = p.getNombre();
					long numProductos = p.getProductos().stream().filter(p -> p.getPrecio() >= 220).count();
				})
				.filter(o -> o.numProductos > 0)
				.sorted(comparing(f -> f.numProductos,reverseOrder()))
				.toList();
		result.forEach(p->System.out.println("Fabricante: "+p.nombreFab+"- Numero de productos: "+p.numProductos));
	}
	/**
	 * 43.Devuelve un listado con los nombres de los fabricantes donde la suma del precio de todos sus productos es superior a 1000 €
	 */
	@Test
	void test43() {
		var listFabs = fabRepo.findAll();
		var result = listFabs.stream()
				.filter(f->f.getProductos().stream().mapToDouble(Producto::getPrecio).sum()>=1000)
				.map(f->new Object(){
					String nombreFab = f.getNombre();
					double sumaTotal = f.getProductos().stream().mapToDouble(Producto::getPrecio).sum();
				})
				.toList();
		result.forEach(f->System.out.println("Fabricante: "+f.nombreFab+"- Suma Total: "+f.sumaTotal));

	}
	
	/**
	 * 44. Devuelve un listado con los nombres de los fabricantes donde la suma del precio de todos sus productos es superior a 1000 €
	 * Ordenado de menor a mayor por cuantía de precio de los productos.
	 */
	@Test
	void test44() {
		//En este ejemplo no se ve que esté ordenado porque al pasar el filtro de 1000 como suma total solo queda un fabricante
		var listFabs = fabRepo.findAll();
		var result = listFabs.stream()
				.filter(f->f.getProductos().stream().mapToDouble(Producto::getPrecio).sum()>=1000)
				.map(f->new Object(){
					String nombreFab = f.getNombre();
					double sumaTotal = f.getProductos().stream().mapToDouble(Producto::getPrecio).sum();
				})
				.sorted(comparing(f->f.sumaTotal))
				.toList();
		result.forEach(f->System.out.println("Fabricante: "+f.nombreFab+"- Suma Total: "+f.sumaTotal));
	}
	
	/**
	 * 45. Devuelve un listado con el nombre del producto más caro que tiene cada fabricante. 
	 * El resultado debe tener tres columnas: nombre del producto, precio y nombre del fabricante. 
	 * El resultado tiene que estar ordenado alfabéticamente de menor a mayor por el nombre del fabricante.
	 */
	@Test
	void test45() {
		var listFabs = fabRepo.findAll();
		var result=listFabs.stream()
				.filter(f->!f.getProductos().isEmpty())
				.map(f -> {
					double precio = f.getProductos().stream()
						.mapToDouble(Producto::getPrecio)
						.max()
						.orElse(0);
					Producto p = f.getProductos().stream()
							.filter(prod -> prod.getPrecio() == precio)
							.findFirst().orElse(null);
					return new Object() {
						String nombreProducto = p != null ? p.getNombre() : "";
						double precioProducto = p != null ? p.getPrecio() : 0;
						String nombreFabricante = f.getNombre();
					};
				})
				.sorted(Comparator.comparing(f -> f.nombreFabricante))
				.toList();

		result.forEach(p -> System.out.println("Nombre producto: " + p.nombreProducto +
												" - Precio: " + p.precioProducto +
												" - Nombre Fabricante: " + p.nombreFabricante));
	}
	
	/**
	 * 46. Devuelve un listado de todos los productos que tienen un precio mayor o igual a la media de todos los productos de su mismo fabricante.
	 * Se ordenará por fabricante en orden alfabético ascendente y los productos de cada fabricante tendrán que estar ordenados por precio descendente.
	 */
	@Test
	void test46() {
		var listFabs = fabRepo.findAll();
		var result = listFabs.stream()
				.flatMap(f -> f.getProductos().stream()
						.filter(p -> p.getPrecio() >= f.getProductos().stream()
								.mapToDouble(Producto::getPrecio
								).average()
								.orElse(0))
						.map(p -> new Object() {
							String nombreFabricante = f.getNombre();
							String nombreProducto = p.getNombre();
							double precioProducto = p.getPrecio();
						})
				)
				.sorted(comparing(f -> f.nombreFabricante))
				.toList();

	}

}
