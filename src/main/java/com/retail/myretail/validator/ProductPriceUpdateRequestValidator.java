package com.retail.myretail.validator;

import com.retail.myretail.message.I18nMessageCode;
import com.retail.myretail.model.request.ProductPriceUpdateRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ProductPriceUpdateRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return ProductPriceUpdateRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        ProductPriceUpdateRequest productPriceUpdateRequest = (ProductPriceUpdateRequest) target;
        validateCurrencyCode(productPriceUpdateRequest, errors);
        validateCurrencyValue(productPriceUpdateRequest, errors);
    }

    private void validateCurrencyCode(ProductPriceUpdateRequest productPriceUpdateRequest, Errors errors) {
        if (StringUtils.isBlank(productPriceUpdateRequest.getCurrency_code())) {
            errors.rejectValue("currency_code", I18nMessageCode.REQUIRED);
        } else if (!StringUtils.isAlpha(productPriceUpdateRequest.getCurrency_code())) {
            errors.rejectValue("currency_code", I18nMessageCode.NOTALPHA);
        }
    }

    private void validateCurrencyValue(ProductPriceUpdateRequest productPriceUpdateRequest, Errors errors) {
        if (productPriceUpdateRequest.getValue() == null) {
            errors.rejectValue("value", I18nMessageCode.REQUIRED);
        } else if (productPriceUpdateRequest.getValue().isNaN()) {
            errors.rejectValue("value", I18nMessageCode.NOTDOUBLE);
        }
    }

}
