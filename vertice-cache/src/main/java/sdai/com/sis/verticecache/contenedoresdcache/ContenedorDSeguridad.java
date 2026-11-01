package sdai.com.sis.verticecache.contenedoresdcache;

import jakarta.enterprise.context.ApplicationScoped;
import sdai.com.sis.vertice.cache.KCache;
import sdai.com.sis.vertice.cache.qualifiers.ContenedoresDCache;

/**
 * Contenedor de caché especializado en el ámbito de seguridad.
 * <p>
 * Gestiona el almacenamiento temporal y recuperación de entidades y credenciales
 * asociadas a la seguridad del sistema dentro del ecosistema Vértice.
 * </p>
 * 
 * @since VERSIONDVERTICECACHEENCURSO
 * @author Sergio_MP
 */
@ApplicationScoped
@ContenedoresDCache(KCache.ContenedoresDCache.CONTESEGUR)
class ContenedorDSeguridad extends AbstractContenedorDCache {

}
