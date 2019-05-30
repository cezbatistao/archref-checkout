package com.cit.checkout.usecase

import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader
import com.cit.checkout.domain.ShoppingCart
import com.cit.checkout.domain.exception.ValidationException
import com.cit.checkout.gateway.validator.DomainValidator
import spock.lang.Specification

import javax.validation.Validation
import javax.validation.Validator

import static br.com.six2six.fixturefactory.Fixture.from

class ShoppingCartValidatorSpec extends Specification {

    private ShoppingCartValidator shoppingCartValidator;

    def setupSpec() {
        FixtureFactoryLoader.loadTemplates("com.cit.checkout.templates")
    }

    def setup() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator()
        DomainValidator<ShoppingCart> domainValidator = new DomainValidator(validator)

        shoppingCartValidator = new ShoppingCartValidator(domainValidator)
    }

    def "test throw exception when shopping cart is null"() {
        given: ""
        ShoppingCart shoppingCart = null

        when: ""
        shoppingCartValidator.execute(shoppingCart)

        then: ""
        IllegalArgumentException ex = thrown()
        ex.message == 'Shopping cart cannot be null'
    }

    def "test throw exception when username from shopping cart is null"() {
        given: ""
        ShoppingCart shoppingCart = from(ShoppingCart.class).gimme("shopping cart with username null")

        when: ""
        shoppingCartValidator.execute(shoppingCart)

        then: ""
        ValidationException ex = thrown()
        ex.message == 'error.validationFields'

        def constraintsErrors = ex.constraintsErrors
        constraintsErrors.size() == 2

        and: ""
        def notBlankError = constraintsErrors.find { it.messageTemplate == '{javax.validation.constraints.NotBlank.message}' }
        notBlankError.propertyPath == 'username'
        notBlankError.message == 'must not be blank'

        def notEmptyError = constraintsErrors.find { it.messageTemplate == '{javax.validation.constraints.NotEmpty.message}' }
        notEmptyError.propertyPath == 'items'
        notEmptyError.message == 'must not be empty'
    }

    def "test throw exception when username from shopping cart is empty"() {
        given: ""
        ShoppingCart shoppingCart = from(ShoppingCart.class).gimme("shopping cart with username empty")

        when: ""
        shoppingCartValidator.execute(shoppingCart)

        then: ""
        ValidationException ex = thrown()
        ex.message == 'error.validationFields'

        def constraintsErrors = ex.constraintsErrors
        constraintsErrors.size() == 1

        and: ""
        def notBlankError = constraintsErrors.find { it.messageTemplate == '{javax.validation.constraints.NotBlank.message}' }
        notBlankError.propertyPath == 'username'
        notBlankError.message == 'must not be blank'
    }

    def "test throw exception when username from shopping cart with blank value"() {
        given: ""
        ShoppingCart shoppingCart = from(ShoppingCart.class).gimme("shopping cart with username blank")

        when: ""
        shoppingCartValidator.execute(shoppingCart)

        then: ""
        ValidationException ex = thrown()
        ex.message == 'error.validationFields'

        def constraintsErrors = ex.constraintsErrors
        constraintsErrors.size() == 1

        and: ""
        def notBlankError = constraintsErrors.find { it.messageTemplate == '{javax.validation.constraints.NotBlank.message}' }
        notBlankError.propertyPath == 'username'
        notBlankError.message == 'must not be blank'
    }

    def "test throw exception when items from shopping cart with product code and quantity with null values"() {
        given: ""
        ShoppingCart shoppingCart = from(ShoppingCart.class).gimme("shopping cart with items null properties")

        when: ""
        shoppingCartValidator.execute(shoppingCart)

        then: ""
        ValidationException ex = thrown()
        ex.message == 'error.validationFields'

        def constraintsErrors = ex.constraintsErrors
        constraintsErrors.size() == 2

        and: ""
        def notBlankError = constraintsErrors.find { it.messageTemplate == '{javax.validation.constraints.NotBlank.message}' }
        notBlankError.propertyPath == 'items[0].productCode'
        notBlankError.message == 'must not be blank'

        def notNullError = constraintsErrors.find { it.messageTemplate == '{javax.validation.constraints.NotNull.message}' }
        notNullError.propertyPath == 'items[0].quantity'
        notNullError.message == 'must not be null'
    }

    def "test throw exception when items from shopping cart with product code and quantity is wrong values"() {
        given: ""
        ShoppingCart shoppingCart = from(ShoppingCart.class).gimme("shopping cart with items wrong properties")

        when: ""
        shoppingCartValidator.execute(shoppingCart)

        then: ""
        ValidationException ex = thrown()
        ex.message == 'error.validationFields'

        def constraintsErrors = ex.constraintsErrors
        constraintsErrors.size() == 2

        and: ""
        def notBlankError = constraintsErrors.find { it.messageTemplate == '{javax.validation.constraints.NotBlank.message}' }
        notBlankError.propertyPath == 'items[0].productCode'
        notBlankError.message == 'must not be blank'

        def notNullError = constraintsErrors.find { it.messageTemplate == '{javax.validation.constraints.Min.message}' }
        notNullError.propertyPath == 'items[0].quantity'
        notNullError.message == 'must be greater than or equal to 1'
    }

    def "test throw exception when items from shopping cart with product code blank and quantity zero"() {
        given: ""
        ShoppingCart shoppingCart = from(ShoppingCart.class).gimme("shopping cart with items have product code blank and zero quantity")

        when: ""
        shoppingCartValidator.execute(shoppingCart)

        then: ""
        ValidationException ex = thrown()
        ex.message == 'error.validationFields'

        def constraintsErrors = ex.constraintsErrors
        constraintsErrors.size() == 2

        and: ""
        def notBlankError = constraintsErrors.find { it.messageTemplate == '{javax.validation.constraints.NotBlank.message}' }
        notBlankError.propertyPath == 'items[0].productCode'
        notBlankError.message == 'must not be blank'

        def notNullError = constraintsErrors.find { it.messageTemplate == '{javax.validation.constraints.Min.message}' }
        notNullError.propertyPath == 'items[0].quantity'
        notNullError.message == 'must be greater than or equal to 1'
    }

    def "test with a valid shopping cart"() {
        given: ""
        ShoppingCart shoppingCart = from(ShoppingCart.class).gimme("shopping cart with one item")

        when: ""
        shoppingCartValidator.execute(shoppingCart)

        then: ""
        noExceptionThrown()
    }
}
