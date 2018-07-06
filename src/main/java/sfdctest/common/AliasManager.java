package sfdctest.common;

import sfdctest.ui.element.Alias;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AliasManager {
    private static AliasManager manager;

    private List<AliasInterface> registeredAliases = new ArrayList<>();

    public synchronized void register(Class registerClass) {
        if (!AliasInterface.class.isAssignableFrom(registerClass)) {
            System.out.printf("[%s] isn't not assignable to [%s]\n",
                    registerClass.getName(),
                    AliasInterface.class.getName()
            );
            return;
        }
        boolean isRegistered = registeredAliases.stream()
                .map(rClass -> rClass.getClass().getCanonicalName())
                .anyMatch(className -> registerClass.getCanonicalName().equals(className));
        if (isRegistered) return;

        try {
            this.registeredAliases.add((AliasInterface) registerClass.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            System.out.printf("Failed to register alias class: [%s]\n", registerClass.getName());
        }
    }

    public List<AliasInterface> all() {
        return Collections.unmodifiableList(this.registeredAliases);
    }

    public static AliasManager getInstance() {
        if (AliasManager.manager != null) return AliasManager.manager;
        AliasManager.manager = new AliasManager();
        // register the built in Alias
        AliasManager.manager.register(Alias.class);
        return AliasManager.manager;
    }
}
