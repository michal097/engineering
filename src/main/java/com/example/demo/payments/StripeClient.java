package com.example.demo.payments;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.net.RequestOptions;
import com.stripe.param.ChargeCreateParams;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.SourceCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StripeClient {

    @Autowired
    StripeClient() {
        Stripe.apiKey = "sk_test_51IUFgHCsIvvwemtrBLaNzXPzMVyz3itfov0CPaq99397ZuyQ615bO4OhW9FMCvxhr3uQ58PJTIAqKMMDu8gr43nr00klI3Inj6";
    }

    public static Account createAccount() throws Exception {
        Map<String, Object> cardPayments =
                new HashMap<>();
        cardPayments.put("requested", true);
        Map<String, Object> transfers = new HashMap<>();
        transfers.put("requested", true);
        Map<String, Object> capabilities =
                new HashMap<>();
        capabilities.put("card_payments", cardPayments);
        capabilities.put("transfers", transfers);
        Map<String, Object> params = new HashMap<>();
        params.put("type", "custom");
        params.put("country", "US");
        params.put("email", "jenny.rosen@example.com");
        params.put("capabilities", capabilities);

        return Account.create(params);
    }

    public static Token bankAccToket() throws StripeException {
        Map<String, Object> bankAccount = new HashMap<>();
        bankAccount.put("country", "US");
        bankAccount.put("currency", "USD");
        bankAccount.put(
                "account_holder_name",
                "Jenny Rosen"
        );
        bankAccount.put(
                "account_holder_type",
                "individual"
        );
        bankAccount.put("routing_number", "110000000");
        bankAccount.put("account_number", "000123456789");
        Map<String, Object> params = new HashMap<>();
        params.put("bank_account", bankAccount);

        return Token.create(params);
    }

    public static BankAccount createBankAccount() throws Exception {
        Account account =
                Account.retrieve(createAccount().getId());

        Map<String, Object> params = new HashMap<>();
        params.put(
                "external_account",
                bankAccToket().getId()
        );

        return
                (BankAccount) account
                        .getExternalAccounts()
                        .create(params);
    }

    public static Token createBankAccToken() throws StripeException {
        Map<String, Object> bankAccount = new HashMap<>();
        bankAccount.put("country", "US");
        bankAccount.put("currency", "usd");
        bankAccount.put(
                "account_holder_name",
                "Jenny Rosen"
        );
        bankAccount.put(
                "account_holder_type",
                "individual"
        );
        bankAccount.put("routing_number", "110000000");
        bankAccount.put("account_number", "000123456789");
        Map<String, Object> params = new HashMap<>();
        params.put("bank_account", bankAccount);

        return Token.create(params);
    }

    public static Account retrieveAcc() throws Exception {
        RequestOptions requestOptions = RequestOptions.builder().setStripeAccount(createBankAccToken().getId()).build();
        System.out.println(createBankAccount());
        Map<String, Object> params = new HashMap<>();
        params.put("email", "person@example.edu");

        Customer.create(params, requestOptions);

        return Account.retrieve(createBankAccToken().getId());
    }


    public Payout makePayout() throws Exception {

        Map<String, Object> params = new HashMap<>();
        params.put("amount", 11);
        params.put("currency", "USD");
        params.put("destination", retrieveAcc().getId());
        params.put("source_type", "bank_account");

        return Payout.create(params);

    }

    private static Source createSource(String IBAN) throws StripeException {
        SourceCreateParams params =
                SourceCreateParams.builder()
                        .setType("sepa_debit")
                        .setCurrency("eur")
                        .setOwner(
                                SourceCreateParams.Owner.builder()
                                        .setName("Jenny Rosen")
                                        .build())
                        .putExtraParam("sepa_debit[iban]", IBAN)
                        .build();

        return Source.create(params);
    }

    private static Customer createCustomer(String IBAN) throws Exception {
        CustomerCreateParams params =
                CustomerCreateParams.builder()
                        .setEmail("paying.user@example.com")
                        .setSource(createSource(IBAN).getId())
                        .build();

        return Customer.create(params);
    }

    public Charge makeChargeRequest(String IBAN, Long amount) throws Exception {
        System.out.println(createCustomer(IBAN));
        ChargeCreateParams params =
                ChargeCreateParams.builder()
                        .setAmount(amount)
                        .setCurrency("eur")
                        .setDescription("Example charge")
                        .setStatementDescriptor("Custom descriptor")
                        .setCustomer(createCustomer(IBAN).getId())
                        .setSource(createSource(IBAN).getId())
                        .build();

        return Charge.create(params);
    }

}