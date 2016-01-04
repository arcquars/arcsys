/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.business;

import java.util.List;
import org.arc.sys.dto.KeyValue;
import org.arc.sys.dto.ProviderPersonDto;
import org.arc.sys.hibernate.entities.Provider;

/**
 *
 * @author angel
 */
public interface ProviderDAO {
    public boolean validNameCompany(String newName);
    
    public boolean createProvider(ProviderPersonDto ppd);
    
    public List<KeyValue> getProviderAll();
    
}
