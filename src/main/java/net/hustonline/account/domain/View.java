package net.hustonline.account.domain;

public class View {

    public interface Summary {}

    public interface AccountBook extends Summary {}

    interface PublicBill extends AccountBook {}
}
