package sdai.com.sis.verticecache;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import sdai.com.sis.vertice.cache.IGlobalCaches;
import sdai.com.sis.vertice.cache.KCache;
import sdai.com.sis.vertice.cache.contenedoresdcache.IContenedorDCache;
import sdai.com.sis.vertice.cache.qualifiers.ContenedoresDCache;

/**
 * Punto centralizado de acceso y resolución de contenedores de caché.
 * <p>
 * Implementa {@link IGlobalCaches} y utiliza inyección CDI dinámica para localizar
 * y suministrar las instancias de {@link IContenedorDCache} correspondientes a los
 * distintos dominios de la aplicación (por ejemplo, seguridad).
 * </p>
 * 
 * @since VERSIONDVERTICECACHEENCURSO
 * @author Sergio_MP
 */
@ApplicationScoped
class GlobalCaches implements IGlobalCaches {

	/**
	 * Instancias CDI disponibles de {@link IContenedorDCache} para resolución dinámica.
	 */
	@Inject
	@Any
	Instance<IContenedorDCache> instancias;

	/**
	 * Obtiene el contenedor de caché asociado al código identificador proporcionado.
	 * 
	 * @param codigoDContenedor código calificador del contenedor requerido
	 * @return la instancia de {@link IContenedorDCache} resuelta
	 */
	@Override
	public IContenedorDCache getContenedorDCache(String codigoDContenedor) {
		ContenedoresDCache.Literal literal = new ContenedoresDCache.Literal(codigoDContenedor);
		IContenedorDCache contenedorDCache = this.instancias.select(IContenedorDCache.class, literal).get();
		return contenedorDCache;
	}

	/**
	 * Obtiene el contenedor de caché especializado en el dominio de seguridad.
	 * 
	 * @return el contenedor de caché de seguridad
	 */
	@Override
	public IContenedorDCache getContenedorDSeguridad() {
		IContenedorDCache contenedorDCache = getContenedorDCache(KCache.ContenedoresDCache.CONTESEGUR);
		return contenedorDCache;
	}

}
