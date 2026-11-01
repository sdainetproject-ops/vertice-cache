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
 * @since VERSIONDVERTICECACHEENCURSO
 * @author Sergio_MP
 */
@ApplicationScoped
class GlobalCaches implements IGlobalCaches {

	@Inject
	@Any
	Instance<IContenedorDCache> instancias;

	@Override
	public IContenedorDCache getContenedorDCache(String codigoDContenedor) {
		ContenedoresDCache.Literal literal = new ContenedoresDCache.Literal(codigoDContenedor);
		IContenedorDCache contenedorDCache = this.instancias.select(IContenedorDCache.class, literal).get();
		return contenedorDCache;
	}

	@Override
	public IContenedorDCache getContenedorDSeguridad() {
		IContenedorDCache contenedorDCache = getContenedorDCache(KCache.ContenedoresDCache.CONTESEGUR);
		return contenedorDCache;
	}

}
