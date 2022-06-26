package app.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class CommonBeans {

    private StringBuilder importResult;

    @Bean
    public StringBuilder importResult(){
        if (this.importResult == null){
            return new StringBuilder();
        }
        return this.importResult;
    }

}
