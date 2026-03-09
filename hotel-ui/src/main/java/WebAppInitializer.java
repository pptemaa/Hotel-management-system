import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.xml.crypto.dsig.spec.XSLTTransformParameterSpec;
import jakarta.servlet.Filter;
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer{
    @Override
    protected Class<?>[] getRootConfigClasses(){
        return new Class[]{AppConfig.class};
    }
    @Override
    protected Class<?>[] getServletConfigClasses(){
        return null;
    }
    @Override
    protected String[] getServletMappings(){
        return new String[]{"/"};
    }

    @Override
    protected Filter[] getServletFilters(){
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return new Filter[]{filter};
    }


}
