package sdai.com.sis.verticecache.serviciosdcache;

import java.util.function.Supplier;

import sdai.com.sis.vertice.cache.KeyCache;
import sdai.com.sis.vertice.cache.contenedoresdcache.IContenedorDCache;

/**
 * Contrato que define la estrategia de gestión de caché (Local, Caffeine, Redis, etc.).
 * <p>
 * Cada implementación concreta gestiona el almacenamiento, recuperación y ciclo
 * de vida de los elementos cacheados según su tecnología subyacente.
 * </p>
 * 
 * @since VERSIONDVERTICECACHEENCURSO
 * @author Sergio_MP
 */
public interface IServicioDCache {

	/**
	 * Asocia el contenedor de caché a esta estrategia.
	 * 
	 * @param contenedorDCache contenedor de caché que utilizará este servicio
	 */
	void establecerContenedor(IContenedorDCache contenedorDCache);

	/**
	 * Obtiene la instancia si existe en la caché; de lo contrario, ejecuta el callback,
	 * la almacena y la retorna.
	 * 
	 * @param <T> tipo de dato de la instancia
	 * @param keyCache clave tipada de caché que identifica la instancia
	 * @param callback proveedor que genera la instancia si no existe en caché
	 * @return la instancia recuperada de la caché o producida por el callback
	 */
	<T> T recuperarInstancia(KeyCache<T> keyCache, Supplier<T> callback);

	/**
	 * Almacena explícitamente una instancia en la caché.
	 * 
	 * @param <T> tipo de dato de la instancia
	 * @param keyCache clave tipada de caché
	 * @param instancia instancia que se desea almacenar
	 */
	<T> void almacenarInstancia(KeyCache<T> keyCache, T instancia);

	/**
	 * Elimina la clave de la caché.
	 * 
	 * @param <T> tipo de dato de la instancia
	 * @param keyCache clave tipada de caché a eliminar
	 */
	<T> void eliminarInstancia(KeyCache<T> keyCache);

	/**
	 * Comprueba si la clave existe en la caché.
	 * 
	 * @param keyCache clave tipada de caché a verificar
	 * @return {@code true} si la clave existe en la caché; {@code false} en caso contrario
	 */
	boolean existeInstancia(KeyCache<?> keyCache);

	/**
	 * Vacía la caché correspondiente a este contenedor.
	 */
	void limpiar();

}
