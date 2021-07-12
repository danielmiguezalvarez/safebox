package com.safeish.safebox.service;

import java.util.UUID;

import com.safeish.safebox.entity.Safebox;

public interface ISafeboxService {
	public UUID safeboxRegister( Safebox safebox) throws Exception;
}
