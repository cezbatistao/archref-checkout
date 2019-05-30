package com.cit.checkout.usecase

import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader
import com.cit.checkout.domain.Payment
import com.cit.checkout.domain.PaymentBillet
import com.cit.checkout.domain.PaymentCard
import com.cit.checkout.domain.exception.ValidationException
import com.cit.checkout.gateway.validator.DomainValidator
import spock.lang.Specification

import javax.validation.Validation
import javax.validation.Validator

import static br.com.six2six.fixturefactory.Fixture.from

class PaymentValidatorSpec extends Specification {

    private PaymentValidator paymentValidator

    def setupSpec() {
        FixtureFactoryLoader.loadTemplates("com.cit.checkout.templates")
    }

    def setup() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator()

        DomainValidator<Payment> domainValidator = new DomainValidator(validator)

        paymentValidator = new PaymentValidator(domainValidator)
    }

    def "test throw exception when payment is null"() {
        given: ""
        Payment payment = null

        when: ""
        paymentValidator.execute(payment)

        then: ""
        IllegalArgumentException ex = thrown()
        ex.message == 'Payment cannot be null'
    }

    def "test payment card with type null and credicard number null"() {
        given: ""
        Payment payment = from(PaymentCard.class).gimme("properties with null")

        when: ""
        paymentValidator.execute(payment)

        then: ""
        ValidationException ex = thrown()
        ex.message == 'error.validationFields'

        def constraintsErrors = ex.constraintsErrors
        constraintsErrors.size() == 2

        and: ""
        def notBlankError = constraintsErrors.find { it.messageTemplate == '{javax.validation.constraints.NotNull.message}' }
        notBlankError.propertyPath == 'type'
        notBlankError.message == 'must not be null'

        def notEmptyError = constraintsErrors.find { it.messageTemplate == '{javax.validation.constraints.NotBlank.message}' }
        notEmptyError.propertyPath == 'creditCard'
        notEmptyError.message == 'must not be blank'
    }

    def "test payment card with credicard number blank value"() {
        given: ""
        Payment payment = from(PaymentCard.class).gimme("credicard with blank value")

        when: ""
        paymentValidator.execute(payment)

        then: ""
        ValidationException ex = thrown()
        ex.message == 'error.validationFields'

        def constraintsErrors = ex.constraintsErrors
        constraintsErrors.size() == 1

        and: ""
        def notEmptyError = constraintsErrors.find { it.messageTemplate == '{javax.validation.constraints.NotBlank.message}' }
        notEmptyError.propertyPath == 'creditCard'
        notEmptyError.message == 'must not be blank'
    }

    def "test payment billet type with null value"() {
        given: ""
        Payment payment = from(PaymentBillet.class).gimme("properties with null values")

        when: ""
        paymentValidator.execute(payment)

        then: ""
        ValidationException ex = thrown()
        ex.message == 'error.validationFields'

        def constraintsErrors = ex.constraintsErrors
        constraintsErrors.size() == 1

        and: ""
        def notBlankError = constraintsErrors.find { it.messageTemplate == '{javax.validation.constraints.NotNull.message}' }
        notBlankError.propertyPath == 'type'
        notBlankError.message == 'must not be null'
    }

    def "test with a valid payment card"() {
        given: ""
        Payment payment = from(PaymentCard.class).gimme("mastercard")

        when: ""
        paymentValidator.execute(payment)

        then: ""
        noExceptionThrown()
    }

    def "test with a valid payment billet"() {
        given: ""
        Payment payment = from(PaymentBillet.class).gimme("billet")

        when: ""
        paymentValidator.execute(payment)

        then: ""
        noExceptionThrown()
    }
}
