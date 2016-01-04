/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.business;

import java.util.List;
import org.arc.sys.dto.KeyValue;
import org.arc.sys.hibernate.entities.Factory;

/**
 *
 * @author angel
 */
public interface FactoryDAO {

    public List<KeyValue> getAllFactories();
    
    public boolean createFactory(Factory newFactory);
    
    public boolean validName(String newName);
   
}
