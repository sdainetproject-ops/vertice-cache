package sdai.com.sis.verticecache.serviciosdcache;

import sdai.com.sis.vertice.cache.KeyCache;
import sdai.com.sis.vertice.cache.contenedoresdcache.IContenedorDCache;

/**
 * Clase base abstracta para las diferentes estrategias de servicio de caché.
 * <p>
 * Proporciona la gestión del enlace con el contenedor de caché asociado y utilidades
 * comunes para la generación y formateo de claves con prefijo de contenedor.
 * </p>
 * 
 * @since VERSIONDVERTICECACHEENCURSO
 * @author Sergio_MP
 */
public abstract class AbstractServicioDCache implements IServicioDCache {

	/**
	 * Contenedor de caché actualmente asociado a esta estrategia.
	 */
	protected IContenedorDCache contenedorDCache;

	/**
	 * Establece el contenedor de caché asociado a esta estrategia de servicio.
	 * 
	 * @param contenedorDCache contenedor de caché a vincular
	 */
	@Override
	public void establecerContenedor(IContenedorDCache contenedorDCache) {
		this.contenedorDCache = contenedorDCache;
	}

	/**
	 * Obtiene el contenedor de caché asociado actualmente a este servicio.
	 * 
	 * @return el contenedor de caché vinculado
	 */
	public IContenedorDCache getContenedorDCache() {
		return contenedorDCache;
	}

	/**
	 * Genera una clave con el prefijo del contenedor para evitar colisiones:
	 * Ejemplo: "CONTESEGUR:sdai.com.sis.verticeseguridad.usuarios.Usuario:ADMIN"
	 * 
	 * @param keyCache clave de caché original
	 * @return cadena de texto representativa de la clave con el prefijo del contenedor
	 */
	protected String formatearClave(KeyCache<?> keyCache) {
		String codigoContenedor = this.contenedorDCache != null ? this.contenedorDCache.getCodigoDContenedor() : "DEFAULT";
		return codigoContenedor + ":" + keyCache.toKeyString();
	}

}
