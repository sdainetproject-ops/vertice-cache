package sdai.com.sis.verticecache.serviciosdcache;

import java.util.function.Supplier;

import sdai.com.sis.vertice.cache.KeyCache;
import sdai.com.sis.vertice.cache.contenedoresdcache.IContenedorDCache;

/**
 * Estrategia de gestión de caché (Local, Caffeine, Redis, etc.).
 * 
 * @since VERSIONDVERTICECACHEENCURSO
 * @author Sergio_MP
 */
public interface IServicioDCache {

	/**
	 * Asocia el contenedor de caché a esta estrategia.
	 */
	void establecerContenedor(IContenedorDCache contenedorDCache);

	/**
	 * Obtiene la instancia si existe en la caché; de lo contrario, ejecuta el callback,
	 * la almacena y la retorna.
	 */
	<T> T recuperarInstancia(KeyCache<T> keyCache, Supplier<T> callback);

	/**
	 * Almacena explícitamente una instancia en la caché.
	 */
	<T> void almacenarInstancia(KeyCache<T> keyCache, T instancia);

	/**
	 * Elimina la clave de la caché.
	 */
	<T> void eliminarInstancia(KeyCache<T> keyCache);

	/**
	 * Comprueba si la clave existe en la caché.
	 */
	boolean existeInstancia(KeyCache<?> keyCache);

	/**
	 * Vacía la caché correspondiente a este contenedor.
	 */
	void limpiar();

}