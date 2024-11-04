
-- 1 ----------------------------------------------------------
    SELECT * FROM producto;
-- 2 ----------------------------------------------------------
    SELECT precio * 1.1 as precio_dolares FROM producto;
-- 3 ----------------------------------------------------------
    SELECT UPPER(nombre) as nombreMayus FROM fabricante;
-- 4 ----------------------------------------------------------
    SELECT nombre,UPPER(SUBSTRING(nombre,1,2)) as iniciales FROM fabricante;
-- 5 ----------------------------------------------------------
    SELECT DISTINCT
        p.codigo_fabricante
    FROM
        producto p
    JOIN
        fabricante f ON p.codigo_fabricante = f.codigo;
-- 6 ----------------------------------------------------------
    SELECT nombre FROM fabricante ORDER BY nombre DESC;
-- 7 ----------------------------------------------------------
    SELECT nombre, precio FROM producto ORDER BY nombre ASC, precio DESC ;
-- 8 ----------------------------------------------------------
    SELECT * FROM fabricante LIMIT 5;
-- 9 ----------------------------------------------------------
    SELECT * FROM fabricante limit 2 OFFSET 2;
-- 10 ----------------------------------------------------------
    SELECT * FROM producto ORDER BY precio LIMIT 1;
-- 11 ----------------------------------------------------------
    SELECT * FROM producto ORDER BY precio DESC LIMIT 1;
-- 12 ----------------------------------------------------------
    SELECT nombre FROM producto WHERE codigo_fabricante = 2;
-- 13 ----------------------------------------------------------
    SELECT nombre FROM producto WHERE precio <= 120;
-- 14 ----------------------------------------------------------
    SELECT * FROM producto WHERE precio >= 400;
-- 15 ----------------------------------------------------------
    SELECT * FROM producto WHERE precio >= 80 AND precio<=300;
-- 16 ----------------------------------------------------------
    SELECT * FROM producto WHERE codigo = 6 AND precio >200;
-- 17 ----------------------------------------------------------
    SELECT * FROM producto WHERE codigo_fabricante IN (1, 3, 5);
-- 18 ----------------------------------------------------------
    SELECT nombre, precio*100 as precio_centimo from producto;
-- 19 ----------------------------------------------------------
    SELECT nombre FROM fabricante WHERE nombre LIKE 's%';
-- 20 ----------------------------------------------------------
    SELECT * FROM producto WHERE nombre LIKE '%Portátil%';
-- 21 ----------------------------------------------------------
    SELECT * FROM producto WHERE nombre LIKE'%Monitor%' AND precio < 215;
-- 22 ----------------------------------------------------------
    SELECT nombre, precio FROM producto WHERE precio >= 180 ORDER BY precio DESC, nombre ASC;
-- 23 ----------------------------------------------------------
    SELECT p.nombre, p.precio, f.nombre FROM producto p JOIN fabricante f
    ON p.codigo_fabricante = f.codigo ORDER BY f.nombre;
-- 24 ----------------------------------------------------------
    SELECT p.nombre, p.precio, f.nombre FROM producto p
        JOIN fabricante f ON p.codigo_fabricante = f.codigo ORDER BY p.precio DESC LIMIT 1;
-- 25 ----------------------------------------------------------
    SELECT * FROM producto p JOIN fabricante f
        ON p.codigo_fabricante = f.codigo
                WHERE f.nombre LIKE 'Crucial' AND p.precio>200;
-- 26 ----------------------------------------------------------
    SELECT p.*, f.nombre NombreFabricante FROM producto p
        JOIN fabricante f ON p.codigo_fabricante=f.codigo
            WHERE f.nombre LIKE '%Asus%' OR f.nombre LIKE '%Hewlett-Packard%' OR f.nombre LIKE '%Seagate%';
-- 27 ----------------------------------------------------------
    SELECT p.nombre, p.precio, f.nombre as nombreFabricante FROM producto p
        JOIN fabricante f ON p.codigo_fabricante=f.codigo
            WHERE p.precio>=180
                ORDER BY p.precio DESC, p.nombre;
-- 28 ----------------------------------------------------------
    SELECT f.nombre Fabricante, p.nombre Producto
    FROM fabricante f
    LEFT JOIN producto p ON f.codigo = p.codigo_fabricante
    ORDER BY f.nombre;
-- 29 ----------------------------------------------------------
    SELECT f.* FROM fabricante f
    WHERE f.codigo NOT IN (SELECT producto.codigo_fabricante FROM producto );
-- 30 ----------------------------------------------------------
    SELECT COUNT(*) Total FROM producto;
-- 31 ----------------------------------------------------------
    SELECT COUNT(*) Total FROM fabricante f
    WHERE f.codigo IN (SELECT p.codigo_fabricante FROM producto p);
-- 32 ----------------------------------------------------------
    SELECT AVG(precio) Total FROM producto;
-- 33 ----------------------------------------------------------
    SELECT MIN(precio) Minimo FROM producto;
-- 34 ----------------------------------------------------------
    SELECT SUM(precio) Suma FROM producto;
-- 35 ----------------------------------------------------------
    SELECT COUNT(*) AsusCantProductos FROM producto p
    JOIN fabricante f ON p.codigo_fabricante = f.codigo
    WHERE f.nombre LIKE 'Asus' ;
-- 36 ----------------------------------------------------------
    SELECT AVG(p.precio) FROM producto p
    JOIN fabricante f ON p.codigo_fabricante = f.codigo
    WHERE f.nombre LIKE 'Asus';
-- 37 ----------------------------------------------------------
    SELECT MAX(p.precio) Max, MIN(p.precio) Min, AVG(p.precio), SUM(p.precio) Total FROM producto p
    JOIN fabricante f ON p.codigo_fabricante = f.codigo
    WHERE f.nombre LIKE 'Crucial'
-- 38 ----------------------------------------------------------

-- 39 ----------------------------------------------------------

-- 40 ----------------------------------------------------------

-- 41 ----------------------------------------------------------

-- 42 ----------------------------------------------------------

-- 43 ----------------------------------------------------------

-- 44 ----------------------------------------------------------

-- 45 ----------------------------------------------------------

-- 46 ----------------------------------------------------------

























