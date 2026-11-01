package sdai.com.sis.verticecache.serviciosdcache.qualifiers;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.TYPE_PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.inject.Qualifier;

/**
 * @since VERSIONDVERTICECACHEENCURSO
 * @author Sergio_MP
 */
@Qualifier
@Retention(RUNTIME)
@Target({ TYPE, FIELD, METHOD, TYPE_PARAMETER })
public @interface ServiciosDCache {

	String value();

	public static class Literal extends AnnotationLiteral<ServiciosDCache> implements ServiciosDCache {

		private static final long serialVersionUID = 1L;

		private final String value;

		public Literal(String value) {
			this.value = value;
		}

		@Override
		public String value() {
			return this.value;
		}

	}

}
