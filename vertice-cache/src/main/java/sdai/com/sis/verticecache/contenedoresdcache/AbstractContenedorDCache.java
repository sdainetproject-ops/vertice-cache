package sdai.com.sis.verticecache.contenedoresdcache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

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
 * Clase base abstracta que implementa la interfaz {@link IContenedorDCache}.
 * <p>
 * Representa un contenedor de caché dentro del ecosistema Vértice y actúa como
 * intermediario entre la lógica de negocio y la estrategia concreta de servicio de
 * caché ({@link IServicioDCache}) configurada dinámicamente mediante CDI.
 * </p>
 * <p>
 * Además, provee un almacén concurrente en memoria para uso exclusivo de aquellas
 * estrategias de almacenamiento que operan a nivel local.
 * </p>
 * 
 * @since VERSIONDVERTICECACHEENCURSO
 * @author Sergio_MP
 */
public abstract class AbstractContenedorDCache implements IContenedorDCache {

	/**
	 * Código identificador de la estrategia de servicio de caché a utilizar.
	 * Configurable mediante la propiedad de MicroProfile Config {@code vertice.servicio.cache}.
	 */
	@ConfigProperty(name = "vertice.servicio.cache", defaultValue = KCache.ServiciosDCache.CACHELOCAL)
	private String codigoDServicio;

	/**
	 * Instancias CDI disponibles de {@link IServicioDCache} para la selección dinámica.
	 */
	@Inject
	@Any
	private Instance<IServicioDCache> instancias;

	/**
	 * Estrategia de servicio de caché activa asignada a este contenedor.
	 */
	private IServicioDCache servicioDCache;

	/**
	 * Código identificador único del contenedor de caché.
	 */
	private String codigoDContenedor;

	/**
	 * Almacén en memoria exclusivo de este contenedor para estrategias locales.
	 */
	private final ConcurrentMap<String, Object> almacenDInstancias = new ConcurrentHashMap<>();

	/**
	 * Inicializa el contenedor tras la inyección de dependencias.
	 * <p>
	 * Determina el identificador del contenedor a partir del calificador
	 * {@link ContenedoresDCache} presente en la clase o su superclase (o por defecto el nombre
	 * simple de la clase), resuelve dinámicamente la implementación de {@link IServicioDCache}
	 * según {@link #codigoDServicio} y asocia este contenedor al servicio resuelto.
	 * </p>
	 */
	@PostConstruct
	public void init() {
		ContenedoresDCache qualifier = this.getClass().getAnnotation(ContenedoresDCache.class);
		if (qualifier == null && this.getClass().getSuperclass() != null) {
			qualifier = this.getClass().getSuperclass().getAnnotation(ContenedoresDCache.class);
		}
		this.codigoDContenedor = qualifier != null ? qualifier.value() : this.getClass().getSimpleName();
		ServiciosDCache.Literal literal = new ServiciosDCache.Literal(this.codigoDServicio);
		this.servicioDCache = this.instancias.select(IServicioDCache.class, literal).get();
		this.servicioDCache.establecerContenedor(this);
	}

	/**
	 * Obtiene el código identificador único asignado a este contenedor de caché.
	 * 
	 * @return el código del contenedor
	 */
	@Override
	public String getCodigoDContenedor() {
		return this.codigoDContenedor;
	}

	/**
	 * Recupera una instancia asociada a la clave proporcionada delegando en la estrategia de caché configurada.
	 * Si la clave no existe en la caché, se ejecuta el callback para generar la instancia, se almacena en la caché y se retorna.
	 * 
	 * @param <T> tipo de dato de la instancia
	 * @param keyCache clave tipada de caché que identifica la instancia
	 * @param callback proveedor que genera la instancia si no se encuentra en caché
	 * @return la instancia recuperada de la caché o producida por el callback
	 */
	@Override
	public <T> T recuperarInstancia(KeyCache<T> keyCache, Supplier<T> callback) {
		return this.servicioDCache.recuperarInstancia(keyCache, callback);
	}

	/**
	 * Almacena explícitamente una instancia en la caché delegando en la estrategia configurada.
	 * 
	 * @param <T> tipo de dato de la instancia
	 * @param keyCache clave tipada de caché
	 * @param instancia instancia que se desea almacenar
	 */
	@Override
	public <T> void almacenarInstancia(KeyCache<T> keyCache, T instancia) {
		this.servicioDCache.almacenarInstancia(keyCache, instancia);
	}

	/**
	 * Elimina la instancia asociada a la clave especificada de la caché.
	 * 
	 * @param <T> tipo de dato de la instancia
	 * @param keyCache clave tipada de caché que se desea desalojar
	 */
	@Override
	public <T> void eliminarInstancia(KeyCache<T> keyCache) {
		this.servicioDCache.eliminarInstancia(keyCache);
	}

	/**
	 * Comprueba si existe una instancia asociada a la clave dada en la caché.
	 * 
	 * @param keyCache clave tipada de caché a verificar
	 * @return {@code true} si la clave existe en la caché; {@code false} en caso contrario
	 */
	@Override
	public boolean existeInstancia(KeyCache<?> keyCache) {
		return this.servicioDCache.existeInstancia(keyCache);
	}

	/**
	 * Limpia o vacía todas las instancias almacenadas en la caché correspondientes a este contenedor.
	 */
	@Override
	public void limpiar() {
		this.servicioDCache.limpiar();
	}

	/**
	 * Proporciona acceso al almacén concurrente en memoria exclusivo de este contenedor.
	 * 
	 * @return mapa concurrente con las instancias almacenadas localmente
	 */
	public ConcurrentMap<String, Object> getAlmacenDInstancias() {
		return this.almacenDInstancias;
	}

}
