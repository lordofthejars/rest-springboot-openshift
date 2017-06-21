/*
 * Copyright 2016-2017 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.openshift.booster;

import com.jayway.restassured.RestAssured;
import io.fabric8.openshift.api.model.Route;
import io.fabric8.openshift.client.OpenShiftClient;
import io.openshift.booster.service.GreetingProperties;
import org.arquillian.cube.kubernetes.api.Session;
import org.assertj.core.api.Assertions;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Before;
import org.junit.runner.RunWith;

import java.util.Objects;

@RunWith(Arquillian.class)
public class OpenShiftIT extends AbstractBoosterApplicationTest {
    @ArquillianResource
    OpenShiftClient client;

    @ArquillianResource
    Session session;

    private final String applicationName = "spring-boot-http";

    @Before
    public void setup() {
        final Route route = this.client.adapt(OpenShiftClient.class)
                .routes()
                .inNamespace(this.session.getNamespace())
                .withName(this.applicationName)
                .get();
        Assertions.assertThat(route)
                .isNotNull();
        RestAssured.baseURI = String.format("http://%s/api/greeting", Objects.requireNonNull(route)
                .getSpec()
                .getHost());
    }

    protected GreetingProperties getProperties() {
        return new GreetingProperties();
    }

}
