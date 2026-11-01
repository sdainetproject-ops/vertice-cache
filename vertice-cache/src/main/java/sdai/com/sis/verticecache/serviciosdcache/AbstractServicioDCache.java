package sdai.com.sis.verticecache.serviciosdcache;

import sdai.com.sis.vertice.cache.KeyCache;
import sdai.com.sis.vertice.cache.contenedoresdcache.IContenedorDCache;

/**
 * @since VERSIONDVERTICECACHEENCURSO
 * @author Sergio_MP
 */
public abstract class AbstractServicioDCache implements IServicioDCache {

	protected IContenedorDCache contenedorDCache;

	@Override
	public void establecerContenedor(IContenedorDCache contenedorDCache) {
		this.contenedorDCache = contenedorDCache;
	}

	public IContenedorDCache getContenedorDCache() {
		return contenedorDCache;
	}

	/**
	 * Genera una clave con el prefijo del contenedor para evitar colisiones:
	 * Ejemplo: "CONTESEGUR:sdai.com.sis.verticeseguridad.usuarios.Usuario:ADMIN"
	 */
	protected String formatearClave(KeyCache<?> keyCache) {
		String codigoContenedor = this.contenedorDCache != null ? this.contenedorDCache.getCodigoDContenedor() : "DEFAULT";
		return codigoContenedor + ":" + keyCache.toKeyString();
	}

}