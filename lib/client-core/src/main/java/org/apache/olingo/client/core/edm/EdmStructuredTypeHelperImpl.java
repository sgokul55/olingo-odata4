/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.olingo.client.core.edm;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.olingo.client.api.edm.xml.EntityType;
import org.apache.olingo.client.api.edm.xml.NavigationProperty;
import org.apache.olingo.client.api.edm.xml.Property;
import org.apache.olingo.client.api.edm.xml.Schema;
import org.apache.olingo.client.api.edm.xml.StructuralType;
import org.apache.olingo.commons.api.edm.Edm;
import org.apache.olingo.commons.api.edm.EdmNavigationProperty;
import org.apache.olingo.commons.api.edm.EdmProperty;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.core.edm.EdmStructuredTypeHelper;

public class EdmStructuredTypeHelperImpl implements EdmStructuredTypeHelper {

  private final Edm edm;

  private final FullQualifiedName structuredTypeName;

  private final StructuralType complexType;

  private final List<? extends Schema> xmlSchemas;

  private Map<String, EdmProperty> properties;

  private Map<String, EdmNavigationProperty> navigationProperties;

  public EdmStructuredTypeHelperImpl(final Edm edm, final FullQualifiedName structuredTypeName,
          final List<? extends Schema> xmlSchemas, final StructuralType structuralType) {

    this.edm = edm;
    this.structuredTypeName = structuredTypeName;
    this.complexType = structuralType;
    this.xmlSchemas = xmlSchemas;
  }

  @Override
  public Map<String, EdmProperty> getProperties() {
    if (properties == null) {
      properties = new LinkedHashMap<String, EdmProperty>();
      for (Property property : complexType.getProperties()) {
        properties.put(property.getName(), new EdmPropertyImpl(edm, structuredTypeName, property));
      }
    }
    return properties;
  }

  @Override
  public Map<String, EdmNavigationProperty> getNavigationProperties() {
    if (navigationProperties == null) {
      navigationProperties = new LinkedHashMap<String, EdmNavigationProperty>();
      for (NavigationProperty navigationProperty : complexType.getNavigationProperties()) {
        if (navigationProperty instanceof org.apache.olingo.client.api.edm.xml.NavigationProperty) {
          navigationProperties.put(navigationProperty.getName(), new EdmNavigationPropertyImpl(
                  edm, structuredTypeName,
                  (org.apache.olingo.client.api.edm.xml.NavigationProperty) navigationProperty));
        }
      }
    }
    return navigationProperties;
  }

  @Override
  public boolean isOpenType() {
    return complexType instanceof org.apache.olingo.client.api.edm.xml.ComplexType
            ? ((org.apache.olingo.client.api.edm.xml.ComplexType) complexType).isOpenType()
            : complexType instanceof EntityType
            ? ((EntityType) complexType).isOpenType()
            : false;
  }

  @Override
  public boolean isAbstract() {
    return complexType instanceof org.apache.olingo.client.api.edm.xml.ComplexType
            ? ((org.apache.olingo.client.api.edm.xml.ComplexType) complexType).isAbstractType()
            : complexType instanceof EntityType
            ? ((EntityType) complexType).isAbstractType()
            : false;
  }
}
