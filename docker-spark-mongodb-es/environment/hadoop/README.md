# Hadoop


El contenedor de Hadoop se crea a partir de la imagen base **ubuntu:14.04**, debido a que la propia comunidad
de docker recomienda encarecidamente utilizar imágenes ligeras que permitan construir arquitecturas con un mejor rendimiento.
La versión seleccionado de hadoop es la ``2.7.5``, debido a la existente compatibilidad con otras tecnologías como Flume, Sqooq y Spark 2.3.

La configuración realizada sobre el contenedor de Hadoop sólo afecta a dos ficheros localizados en la ruta **/opt/sds/hadoop/etc/hadoop**
- **core-site.xml** - (propriedad --> `fs.defaultFS`)
- **hdfs-site.xml** - (propiedad --> `dfs.replication` y `dfs.permissions`)
