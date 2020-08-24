package androidx.navigation.ui;

import androidx.annotation.NonNull;
import androidx.navigation.NavDestination;

public interface TransitionResolver {
    boolean isUseTransitionFor(@NonNull  NavDestination destination);
}
