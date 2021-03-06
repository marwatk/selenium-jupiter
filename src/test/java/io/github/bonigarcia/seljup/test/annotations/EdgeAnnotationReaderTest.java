/*
 * (C) Copyright 2017 Boni Garcia (http://bonigarcia.github.io/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package io.github.bonigarcia.seljup.test.annotations;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.lang.reflect.Parameter;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import io.github.bonigarcia.seljup.AnnotationsReader;
import io.github.bonigarcia.seljup.handler.DriverHandler;
import io.github.bonigarcia.seljup.handler.EdgeDriverHandler;
import io.github.bonigarcia.seljup.test.advance.EdgeWithGlobalOptionsJupiterTest;

public class EdgeAnnotationReaderTest {

    DriverHandler annotationsReader = new EdgeDriverHandler(null, null, null,
            new AnnotationsReader());

    static Stream<Class<?>> testClassProvider() {
        return Stream.of(EdgeWithGlobalOptionsJupiterTest.class);
    }

    @ParameterizedTest
    @MethodSource("testClassProvider")
    void testEdgeOptions(Class<?> testClass) throws Exception {
        Parameter parameter = testClass.getMethod("edgeTest", EdgeDriver.class)
                .getParameters()[0];
        Optional<Object> testInstance = Optional.of(testClass.newInstance());
        EdgeOptions edgeOptions = (EdgeOptions) annotationsReader
                .getOptions(parameter, testInstance);
        assertThat(edgeOptions.getCapability("pageLoadStrategy"),
                equalTo("eager"));
    }

    @Test
    void testAnnotatedEdgeOptionsIsSelectedOverOtherAnnotatedOptions()
            throws Exception {
        Optional<Object> testInstance = Optional
                .of(new ClassWithMultipleOptions());
        EdgeOptions edgeOptions = (EdgeOptions) annotationsReader
                .getOptions(null, testInstance);
        assertThat(edgeOptions, notNullValue());
    }
}
