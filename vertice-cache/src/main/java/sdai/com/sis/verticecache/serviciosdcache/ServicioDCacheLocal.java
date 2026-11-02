package sdai.com.sis.verticecache.serviciosdcache;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

import jakarta.enterprise.context.Dependent;
import sdai.com.sis.vertice.cache.KCache;
import sdai.com.sis.vertice.cache.KeyCache;
import sdai.com.sis.verticecache.contenedoresdcache.AbstractContenedorDCache;
import sdai.com.sis.verticecache.serviciosdcache.qualifiers.ServiciosDCache;

/**
 * Estrategia de servicio de caché local en memoria.
 * <p>
 * Implementa el almacenamiento en memoria directa apoyándose en el
 * almacén concurrente proporcionado por {@link AbstractContenedorDCache}.
 * </p>
 * 
 * @since VERSIONDVERTICECACHEENCURSO
 * @author Sergio_MP
 */
@Dependent
@ServiciosDCache(KCache.ServiciosDCache.CACHELOCAL)
public class ServicioDCacheLocal extends AbstractServicioDCache {

	/**
	 * Obtiene el mapa de almacenamiento concurrente asociado al contenedor actual.
	 * 
	 * @return mapa concurrente en memoria del contenedor
	 * @throws IllegalStateException si el contenedor asociado no es instancia de {@link AbstractContenedorDCache}
	 */
	private ConcurrentMap<String, Object> getMapaAlmacen() {
		if (this.contenedorDCache instanceof AbstractContenedorDCache) {
			return ((AbstractContenedorDCache) this.contenedorDCache).getAlmacenDInstancias();
		}
		throw new IllegalStateException("El contenedor asociado no es una instancia válida de AbstractContenedorDCache");
	}

	@Override
	public <T> T recuperarInstancia(KeyCache<T> keyCache, Supplier<T> callback) {
		Objects.requireNonNull(keyCache, "La clave de cache no puede ser nula");
		ConcurrentMap<String, Object> mapa = getMapaAlmacen();
		String clave = keyCache.toKeyString();

		Object valorExistente = mapa.get(clave);
		if (valorExistente != null) {
			return keyCache.getClase().cast(valorExistente);
		}
		if (callback != null) {
			Object valorCalculado = mapa.computeIfAbsent(clave, k -> callback.get());
			if (valorCalculado != null) {
				return keyCache.getClase().cast(valorCalculado);
			}
		}

		return null;
	}

	@Override
	public <T> void almacenarInstancia(KeyCache<T> keyCache, T instancia) {
		Objects.requireNonNull(keyCache, "La clave de cache no puede ser nula");
		if (instancia != null) {
			getMapaAlmacen().put(keyCache.toKeyString(), instancia);
		} else {
			eliminarInstancia(keyCache);
		}
	}

	@Override
	public <T> void eliminarInstancia(KeyCache<T> keyCache) {
		if (keyCache != null) {
			getMapaAlmacen().remove(keyCache.toKeyString());
		}
	}

	@Override
	public boolean existeInstancia(KeyCache<?> keyCache) {
		if (keyCache == null) {
			return false;
		}
		return getMapaAlmacen().containsKey(keyCache.toKeyString());
	}

	@Override
	public void limpiar() {
		getMapaAlmacen().clear();
	}

}
