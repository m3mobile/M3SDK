package net.m3mobile.core.inspection

import net.m3mobile.core.InternalM3Api
import java.lang.reflect.Method

@InternalM3Api
public interface Inspector: (Method) -> Unit