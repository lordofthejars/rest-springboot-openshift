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

import com.jayway.restassured.builder.RequestSpecBuilder;
import io.openshift.booster.service.GreetingProperties;
import java.net.URL;
import java.util.Objects;
import org.arquillian.cube.openshift.impl.enricher.RouteURL;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

@RunWith(Arquillian.class)
public class OpenShiftIT {

    @RouteURL("spring-boot-http")
    private URL route;

    private RequestSpecBuilder requestSpecBuilder;

    @Before
    public void setupRestAssured() {
        this.requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setBaseUri(String.format("http://%s/api/greeting", Objects.requireNonNull(route)
            .getHost()));
    }

    @Test
    public void testGreetingEndpoint() {
        given(requestSpecBuilder.build())
            .when().get()
            .then()
            .statusCode(200)
            .body("content", is(String.format(getProperties().getMessage(), "World")));
    }

    @Test
    public void testGreetingEndpointWithNameParameter() {
        given(requestSpecBuilder.build())
            .param("name", "John")
            .when()
            .get()
            .then()
            .statusCode(200)
            .body("content", is(String.format(getProperties().getMessage(), "John")));
    }

    protected GreetingProperties getProperties() {
        return new GreetingProperties();
    }

}
