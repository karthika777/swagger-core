package io.swagger.util;

import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.models.parameters.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;

public class ParameterProcessor {
    static Logger LOGGER = LoggerFactory.getLogger(ParameterProcessor.class);

    public static Parameter applyAnnotations(OpenAPI swagger, Parameter parameter, Type type, List<Annotation> annotations) {
        final AnnotationsHelper helper = new AnnotationsHelper(annotations, type);
        if (helper.isContext()) {
            return null;
        }
        final ParamWrapper<?> param = null;
        return parameter;
        /// TODO
        /*
        if (param.isHidden()) {
            return null;
        }
        final String defaultValue = helper.getDefaultValue();
        if (param.isRequired()) {
            parameter.setRequired(true);
        }
        if(param.getReadOnly()) {
//            parameter.readOnly(param.getReadOnly());
        }
        if(param.getAllowEmptyValue()) {
            parameter.allowEmptyValue(param.getAllowEmptyValue());
        }
        if (StringUtils.isNotEmpty(param.getName())) {
            parameter.setName(param.getName());
        }
        if (StringUtils.isNotEmpty(param.getDescription())) {
            parameter.setDescription(param.getDescription());
        }
        if (StringUtils.isNotEmpty(param.getExample())) {
            parameter.setExample(param.getExample());
        }
        if (StringUtils.isNotEmpty(param.getAccess())) {
//            parameter.setAccess(param.getAccess());
        }

        if(StringUtils.isNoneEmpty(param.getCollectionFormat())) {
            parameter.setCollectionFormat(param.getCollectionFormat());
        }
        if (StringUtils.isNotEmpty(param.getDataType())) {
            if ("java.io.File".equalsIgnoreCase(param.getDataType())) {
                p.setProperty(new FileProperty());
            } else if("long".equalsIgnoreCase(param.getDataType())) {
                p.setProperty(new LongProperty());
            } else {
                p.setType(param.getDataType());
            }
        }
        if (helper.getMin() != null) {
            parameter.setMinimum(helper.getMin());
            if (helper.isMinExclusive()) {
                parameter.setExclusiveMinimum(true);
            }
        }

        if (helper.getMax() != null) {
            parameter.setMaximum(helper.getMax());
            if (helper.isMaxExclusive()) {
                parameter.setExclusiveMaximum(true);
            }
        }

        if (helper.getMinItems() != null) {
            parameter.setMinItems(helper.getMinItems());
        }
        if (helper.getMaxItems() != null) {
            parameter.setMaxItems(helper.getMaxItems());
        }

        if (helper.getMinLength() != null) {
            parameter.setMinLength(helper.getMinLength());
        }
        if (helper.getMaxLength() != null) {
            parameter.setMaxLength(helper.getMaxLength());
        }

        if (helper.getPattern() != null) {
            parameter.setPattern(helper.getPattern());
        }

        if (helper.isRequired() != null) {
            parameter.setRequired(true);
        }
        if(helper.getType() != null) {
            parameter.setType(helper.getType());
        }
        if(helper.getFormat() != null) {
            parameter.setFormat(helper.getFormat());
        }

        AllowableValues allowableValues = AllowableValuesUtils.create(param.getAllowableValues());

        if (parameter.getItems() != null || param.isAllowMultiple()) {
            if (p.getItems() == null) {
                // Convert to array
                final Map<PropertyBuilder.PropertyId, Object> args = new EnumMap<PropertyBuilder.PropertyId, Object>(PropertyBuilder.PropertyId.class);
                args.put(PropertyBuilder.PropertyId.DEFAULT, p.getDefaultValue());
                p.setDefaultValue(null);
                args.put(PropertyBuilder.PropertyId.ENUM, p.getEnum());
                p.setEnum(null);
                args.put(PropertyBuilder.PropertyId.MINIMUM, p.getMinimum());
                p.setMinimum(null);
                args.put(PropertyBuilder.PropertyId.EXCLUSIVE_MINIMUM, p.isExclusiveMinimum());
                p.setExclusiveMinimum(null);
                args.put(PropertyBuilder.PropertyId.MAXIMUM, p.getMaximum());
                p.setMaximum(null);
                args.put(PropertyBuilder.PropertyId.EXCLUSIVE_MAXIMUM, p.isExclusiveMaximum());
                args.put(PropertyBuilder.PropertyId.MIN_LENGTH, p.getMinLength());
                p.setMinLength(null);
                args.put(PropertyBuilder.PropertyId.MAX_LENGTH, p.getMaxLength());
                p.setMaxLength(null);
                args.put(PropertyBuilder.PropertyId.PATTERN, p.getPattern());
                p.setPattern(null);
                args.put(PropertyBuilder.PropertyId.EXAMPLE, p.getExample());
                p.setExclusiveMaximum(null);
                Property items = PropertyBuilder.build(p.getType(), p.getFormat(), args);
                p.type(ArrayProperty.TYPE).format(null).items(items);
            }

            final Map<PropertyBuilder.PropertyId, Object> args = new EnumMap<PropertyBuilder.PropertyId, Object>(PropertyBuilder.PropertyId.class);
            if (StringUtils.isNotEmpty(defaultValue)) {
                args.put(PropertyBuilder.PropertyId.DEFAULT, defaultValue);
            }

//            **
//             * Use jsr-303 annotations (and other bean validation annotations) if present. This essentially implies
//             * that the bean validation constraints now apply to the items and not to the parent collection/array.
//             * Although this  will work for swagger definition purposes, there is no default validator for many of
//             * the validator annotations when applied to a collection/array. For example, a @Min annotation applied
//             * to a List&gt;Long&lt; will result in a swagger definition which contains an array property with items
//             * of type number and having a 'minimum' validation constraint. However, there is no default bean
//             * validator for @Min when applied to a List&gt;Long&lt;, and the developer would need to implement such
//             * a validator themselves.
//             *

            if (helper.getMin() != null) {
                args.put(PropertyBuilder.PropertyId.MINIMUM,
                        helper.getMin());
                if (helper.isMinExclusive()) {
                    args.put(PropertyBuilder.PropertyId.EXCLUSIVE_MINIMUM, true);
                }
            }

            if (helper.getMax() != null) {
                args.put(PropertyBuilder.PropertyId.MAXIMUM,
                        helper.getMax());
                if (helper.isMaxExclusive()) {
                    args.put(PropertyBuilder.PropertyId.EXCLUSIVE_MAXIMUM, true);
                }
            }

            if (helper.getMinLength() != null) {
                args.put(PropertyBuilder.PropertyId.MIN_LENGTH, helper.getMinLength());
            }
            if (helper.getMaxLength() != null) {
                args.put(PropertyBuilder.PropertyId.MAX_LENGTH, helper.getMaxLength());
            }
            if (helper.getPattern() != null) {
                args.put(PropertyBuilder.PropertyId.PATTERN, helper.getPattern());
            }

            //Overwrite Bean validation values with allowable values if present
            if (allowableValues != null) {
                args.putAll(allowableValues.asPropertyArguments());
            }
            PropertyBuilder.merge(p.getItems(), args);
        return parameter;
        */
    }

    private static void processJsr303Annotations(AnnotationsHelper helper, Parameter p) {
        if (helper == null) {
            return;
        }
        if (helper.getMin() != null) {
//            p.setMinimum(helper.getMin());
        }
        if (helper.getMax() != null) {
//            p.setMaximum(helper.getMax());
        }
    }

    /**
     * Wraps either an @ApiParam or and @ApiImplicitParam
     */

    public interface ParamWrapper<T extends Annotation> {
        String getName();

        String getDescription();

        String getDefaultValue();

        String getAllowableValues();

        boolean isRequired();

        String getAccess();

        boolean isAllowMultiple();

        String getDataType();

        String getParamType();

        T getAnnotation();

        boolean isHidden();

        String getExample();

        String getType();

        String getFormat();

        boolean getReadOnly();

        boolean getAllowEmptyValue();

        String getCollectionFormat();
    }

    /**
     * The <code>AnnotationsHelper</code> class defines helper methods for
     * accessing supported parameter annotations.
     */
    private static class AnnotationsHelper {
//        private static final ApiParam DEFAULT_API_PARAM = getDefaultApiParam(null);
        private boolean context;
//        private ParamWrapper<?> apiParam = new ApiParamWrapper(DEFAULT_API_PARAM);
        private String type;
        private String format;
        private String defaultValue;
        private Integer minItems;
        private Integer maxItems;
        private Boolean required;
        private BigDecimal min;
        private boolean minExclusive = false;
        private BigDecimal max;
        private boolean maxExclusive = false;
        private Integer minLength;
        private Integer maxLength;
        private String pattern;
        private Boolean allowEmptyValue;
        private String collectionFormat;

        /**
         * Constructs an instance.
         *
         * @param annotations array or parameter annotations
         */
        public AnnotationsHelper(List<Annotation> annotations, Type _type) {
            String rsDefault = null;
            Size size = null;
            for (Annotation item : annotations) {
                if ("javax.ws.rs.core.Context".equals(item.annotationType().getName())) {
                    context = true;
//                } else if (item instanceof ApiParam) {
//                    apiParam = new ApiParamWrapper((ApiParam) item);
//                } else if (item instanceof ApiImplicitParam) {
//                    apiParam = new ApiImplicitParamWrapper((ApiImplicitParam) item);
                } else if ("javax.ws.rs.DefaultValue".equals(item.annotationType().getName())) {
                    try {
                        rsDefault = (String) item.annotationType().getMethod("value").invoke(item);
                    } catch (Exception ex) {
                        LOGGER.error("Invocation of value method failed", ex);
                    }
                } else if (item instanceof Size) {
                    size = (Size) item;
                    /**
                     * This annotation is handled after the loop, as the allow multiple field of the
                     * ApiParam annotation can affect how the Size annotation is translated
                     * Swagger property constraints
                     */
                } else if (item instanceof NotNull) {
                    required = true;
                } else if (item instanceof Min) {
                    min = new BigDecimal(((Min) item).value());
                } else if (item instanceof Max) {
                    max = new BigDecimal(((Max) item).value());
                } else if (item instanceof DecimalMin) {
                    DecimalMin decimalMinAnnotation = (DecimalMin) item;
                    min = new BigDecimal(decimalMinAnnotation.value());
                    minExclusive = !decimalMinAnnotation.inclusive();
                } else if (item instanceof DecimalMax) {
                    DecimalMax decimalMaxAnnotation = (DecimalMax) item;
                    max = new BigDecimal(decimalMaxAnnotation.value());
                    maxExclusive = !decimalMaxAnnotation.inclusive();
                } else if (item instanceof Pattern) {
                    pattern = ((Pattern) item).regexp();
                }
            }
            /*
            if (size != null) {
                Property property = ModelConverters.getInstance().readAsProperty(_type);
                boolean defaultToArray = apiParam != null && apiParam.isAllowMultiple();
                if (!defaultToArray && property instanceof AbstractNumericProperty) {
                    min = new BigDecimal(size.min());
                    max = new BigDecimal(size.max());
                } else if (!defaultToArray && property instanceof StringProperty) {
                    minLength = size.min();
                    maxLength = size.max();
                } else {
                    minItems = size.min();
                    maxItems = size.max();
                }
            }
            */
//            defaultValue = StringUtils.isNotEmpty(apiParam.getDefaultValue()) ? apiParam.getDefaultValue() : rsDefault;
//            type = StringUtils.isNotEmpty(apiParam.getType()) ? apiParam.getType() : null;
//            format = StringUtils.isNotEmpty(apiParam.getFormat()) ? apiParam.getFormat() : null;
//            allowEmptyValue = apiParam.isAllowMultiple() ? true : null;
//            collectionFormat = StringUtils.isNoneEmpty(apiParam.getCollectionFormat()) ? apiParam.getCollectionFormat() : null;
        }

        private boolean isAssignableToNumber(Class<?> clazz) {
            return Number.class.isAssignableFrom(clazz)
                    || int.class.isAssignableFrom(clazz)
                    || short.class.isAssignableFrom(clazz)
                    || long.class.isAssignableFrom(clazz)
                    || float.class.isAssignableFrom(clazz)
                    || double.class.isAssignableFrom(clazz);
        }

//         * Returns a default @{@link ApiParam} annotation for parameters without it.
//         *
//         * @param annotationHolder a placeholder for default @{@link ApiParam}
//         *                         annotation
//         * @return @{@link ApiParam} annotation
//        private static ApiParam getDefaultApiParam(@ApiParam String annotationHolder) {
//            for (Method method : AnnotationsHelper.class.getDeclaredMethods()) {
//                if ("getDefaultApiParam".equals(method.getName())) {
//                    return (ApiParam) method.getParameterAnnotations()[0][0];
//                }
//            }
//            throw new IllegalStateException("Failed to locate default @ApiParam");
//        }

        /**
         */
        public boolean isContext() {
            return context;
        }

//        /**
//         * Returns @{@link ApiParam} annotation. If no @{@link ApiParam} is present
//         * a default one will be returned.
//         *
//         * @return @{@link ApiParam} annotation
//         */
//        public ParamWrapper<?> getApiParam() {
//            return apiParam;
//        }

        /**
         * Returns default value from annotation.
         *
         * @return default value from annotation
         */
        public String getDefaultValue() {
            return defaultValue;
        }

        public Integer getMinItems() {
            return minItems;
        }

        public Integer getMaxItems() {
            return maxItems;
        }

        public Boolean isRequired() {
            return required;
        }

        public BigDecimal getMax() {
            return max;
        }

        public boolean isMaxExclusive() {
            return maxExclusive;
        }

        public BigDecimal getMin() {
            return min;
        }

        public String getType() {
            return type;
        }

        public String getFormat() {
            return format;
        }

        public boolean isMinExclusive() {
            return minExclusive;
        }

        public Integer getMinLength() {
            return minLength;
        }

        public Integer getMaxLength() {
            return maxLength;
        }

        public String getPattern() {
            return pattern;
        }

        public Boolean getAllowEmptyValue() {
            return allowEmptyValue;
        }

        public String getCollectionFormat() {
            return collectionFormat;
        }
    }

    /**
     * Wrapper implementation for ApiParam annotation
     */

//    private final static class ApiParamWrapper implements ParamWrapper<ApiParam> {
//
//        private final ApiParam apiParam;
//
//        private ApiParamWrapper(ApiParam apiParam) {
//            this.apiParam = apiParam;
//        }
//
//        @Override
//        public String getName() {
//            return apiParam.name();
//        }
//
//        @Override
//        public String getDescription() {
//            return apiParam.value();
//        }
//
//        @Override
//        public String getDefaultValue() {
//            return apiParam.defaultValue();
//        }
//
//        @Override
//        public String getAllowableValues() {
//            return apiParam.allowableValues();
//        }
//
//        @Override
//        public boolean isRequired() {
//            return apiParam.required();
//        }
//
//        @Override
//        public String getAccess() {
//            return apiParam.access();
//        }
//
//        @Override
//        public boolean isAllowMultiple() {
//            return apiParam.allowMultiple();
//        }
//
//        @Override
//        public String getDataType() {
//            return null;
//        }
//
//        @Override
//        public String getParamType() {
//            return null;
//        }
//
//        @Override
//        public ApiParam getAnnotation() {
//            return apiParam;
//        }
//
//        @Override
//        public boolean isHidden() {
//            return apiParam.hidden();
//        }
//
//        @Override
//        public String getExample() {
//            return apiParam.example();
//        }
//
//        public Example getExamples() {
//            return apiParam.examples();
//        }
//
//        public String getType() {
//            return apiParam.type();
//        }
//
//        public String getFormat() {
//            return apiParam.format();
//        }
//
//        public boolean getReadOnly() {
//            return apiParam.readOnly();
//        }
//
//        public boolean getAllowEmptyValue() {
//            return apiParam.allowEmptyValue();
//        }
//
//        public String getCollectionFormat() {
//            return apiParam.collectionFormat();
//        }
//    }
//
//    /**
//     * Wrapper implementation for ApiImplicitParam annotation
//     */
//    private final static class ApiImplicitParamWrapper implements ParamWrapper<ApiImplicitParam> {
//
//        private final ApiImplicitParam apiParam;
//
//        private ApiImplicitParamWrapper(ApiImplicitParam apiParam) {
//            this.apiParam = apiParam;
//        }
//
//        @Override
//        public String getName() {
//            return apiParam.name();
//        }
//
//        @Override
//        public String getDescription() {
//            return apiParam.value();
//        }
//
//        @Override
//        public String getDefaultValue() {
//            return apiParam.defaultValue();
//        }
//
//        @Override
//        public String getAllowableValues() {
//            return apiParam.allowableValues();
//        }
//
//        @Override
//        public boolean isRequired() {
//            return apiParam.required();
//        }
//
//        @Override
//        public String getAccess() {
//            return apiParam.access();
//        }
//
//        @Override
//        public boolean isAllowMultiple() {
//            return apiParam.allowMultiple();
//        }
//
//        @Override
//        public String getDataType() {
//            return apiParam.dataType();
//        }
//
//        @Override
//        public String getParamType() {
//            return apiParam.paramType();
//        }
//
//        @Override
//        public ApiImplicitParam getAnnotation() {
//            return apiParam;
//        }
//
//        @Override
//        public boolean isHidden() {
//            return false;
//        }
//
//        @Override
//        public String getExample() {
//            return apiParam.example();
//        }
//
//        public Example getExamples() {
//            return apiParam.examples();
//        }
//
//        public String getType() {
//            return apiParam.type();
//        }
//
//        public String getFormat() {
//            return apiParam.format();
//        }
//
//        public boolean getReadOnly() {
//            return apiParam.readOnly();
//        }
//
//        public boolean getAllowEmptyValue() {
//            return apiParam.allowEmptyValue();
//        }
//
//        public String getCollectionFormat() {
//            return apiParam.collectionFormat();
//        }
//    }
}
