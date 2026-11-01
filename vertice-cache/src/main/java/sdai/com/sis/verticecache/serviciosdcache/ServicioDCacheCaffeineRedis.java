package sdai.com.sis.verticecache.serviciosdcache;

import java.util.function.Supplier;

import jakarta.enterprise.context.Dependent;
import sdai.com.sis.vertice.cache.KCache;
import sdai.com.sis.vertice.cache.KeyCache;
import sdai.com.sis.verticecache.serviciosdcache.qualifiers.ServiciosDCache;

/**
 * Estrategia de servicio de caché híbrida de dos niveles (L1 Caffeine + L2 Redis).
 * <p>
 * Combina la velocidad de acceso en memoria local mediante Caffeine con la persistencia
 * y distribución remota a través de Redis.
 * </p>
 * 
 * @since VERSIONDVERTICECACHEENCURSO
 * @author Sergio_MP
 */
@Dependent
@ServiciosDCache(KCache.ServiciosDCache.CACHECARED)
class ServicioDCacheCaffeineRedis extends AbstractServicioDCache {

	@Override
	public <T> T recuperarInstancia(KeyCache<T> keyCache, Supplier<T> callback) {
		
		return null;
	}

	@Override
	public <T> void almacenarInstancia(KeyCache<T> keyCache, T instancia) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void eliminarInstancia(KeyCache<T> keyCache) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean existeInstancia(KeyCache<?> keyCache) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void limpiar() {
		// TODO Auto-generated method stub
		
	}

}
