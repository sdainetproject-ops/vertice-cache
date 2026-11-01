package sdai.com.sis.verticecache.contenedoresdcache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import sdai.com.sis.vertice.cache.KCache;
import sdai.com.sis.vertice.cache.KeyCache;
import sdai.com.sis.vertice.cache.contenedoresdcache.IContenedorDCache;
import sdai.com.sis.vertice.cache.qualifiers.ContenedoresDCache;
import sdai.com.sis.verticecache.serviciosdcache.IServicioDCache;
import sdai.com.sis.verticecache.serviciosdcache.qualifiers.ServiciosDCache;

/**
 * @since VERSIONDVERTICECACHEENCURSO
 * @author Sergio_MP
 */
public abstract class AbstractContenedorDCache implements IContenedorDCache {

	@ConfigProperty(name = "vertice.servicio.cache", defaultValue = KCache.ServiciosDCache.CACHELOCAL)
	private String codigoDServicioDefecto;

	@Inject
	@Any
	private Instance<IServicioDCache> instancias;

	private IServicioDCache servicioDCache;
	private String codigoDContenedor;

	/**
	 * Almacén en memoria exclusivo de este contenedor para estrategias locales.
	 */
	private final ConcurrentMap<String, Object> almacenDInstancias = new ConcurrentHashMap<>();

	@PostConstruct
	public void init() {
		// 1. Obtener el identificador del contenedor desde la anotación @ContenedoresDCache
		ContenedoresDCache qualifier = this.getClass().getAnnotation(ContenedoresDCache.class);
		if (qualifier == null && this.getClass().getSuperclass() != null) {
			// Soporte para proxies CDI de Quarkus
			qualifier = this.getClass().getSuperclass().getAnnotation(ContenedoresDCache.class);
		}
		this.codigoDContenedor = qualifier != null ? qualifier.value() : this.getClass().getSimpleName();

		// 2. Permitir sobreescribir el tipo de servicio por contenedor en application.properties:
		// Ejemplo: vertice.cache.CONTESEGUR.servicio=CACHECAFFE
		// Si no existe, usa vertice.servicio.cache (por defecto CACHELOCAL)
		String propiedadEspecifica = "vertice.cache." + this.codigoDContenedor + ".servicio";
		String servicioFinal = ConfigProvider.getConfig()
				.getOptionalValue(propiedadEspecifica, String.class)
				.orElse(this.codigoDServicioDefecto);

		// 3. Resolver la estrategia CDI correspondiente y vincular este contenedor
		ServiciosDCache.Literal literal = new ServiciosDCache.Literal(servicioFinal);
		this.servicioDCache = this.instancias.select(IServicioDCache.class, literal).get();
		this.servicioDCache.establecerContenedor(this);
	}

	@Override
	public String getCodigoDContenedor() {
		return this.codigoDContenedor;
	}

	@Override
	public <T> T recuperarInstancia(KeyCache<T> keyCache, Supplier<T> callback) {
		return this.servicioDCache.recuperarInstancia(keyCache, callback);
	}

	@Override
	public <T> void almacenarInstancia(KeyCache<T> keyCache, T instancia) {
		this.servicioDCache.almacenarInstancia(keyCache, instancia);
	}

	@Override
	public <T> void eliminarInstancia(KeyCache<T> keyCache) {
		this.servicioDCache.eliminarInstancia(keyCache);
	}

	@Override
	public boolean existeInstancia(KeyCache<?> keyCache) {
		return this.servicioDCache.existeInstancia(keyCache);
	}

	@Override
	public void limpiar() {
		this.servicioDCache.limpiar();
	}

	/**
	 * Permite a los servicios locales acceder al mapa de este contenedor.
	 */
	public ConcurrentMap<String, Object> getAlmacenDInstancias() {
		return this.almacenDInstancias;
	}

}