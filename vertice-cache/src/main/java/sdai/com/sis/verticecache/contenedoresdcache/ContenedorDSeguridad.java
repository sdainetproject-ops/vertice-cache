package sdai.com.sis.verticecache.contenedoresdcache;

import jakarta.enterprise.context.ApplicationScoped;
import sdai.com.sis.vertice.cache.KCache;
import sdai.com.sis.vertice.cache.qualifiers.ContenedoresDCache;

/**
 * @since VERSIONDVERTICECACHEENCURSO
 * @author Sergio_MP
 */
@ApplicationScoped
@ContenedoresDCache(KCache.ContenedoresDCache.CONTESEGUR)
class ContenedorDSeguridad extends AbstractContenedorDCache {

}
