
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

-- 23 ----------------------------------------------------------

-- 24 ----------------------------------------------------------

-- 25 ----------------------------------------------------------

-- 26 ----------------------------------------------------------

-- 27 ----------------------------------------------------------

-- 28 ----------------------------------------------------------

-- 29 ----------------------------------------------------------

-- 30 ----------------------------------------------------------


