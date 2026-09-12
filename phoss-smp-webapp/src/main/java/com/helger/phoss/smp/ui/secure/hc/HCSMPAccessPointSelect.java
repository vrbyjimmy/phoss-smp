/*
 * Copyright (C) 2014-2026 Philip Helger and contributors
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.helger.phoss.smp.ui.secure.hc;

import org.jspecify.annotations.NonNull;

import com.helger.annotation.Nonempty;
import com.helger.base.string.StringHelper;
import com.helger.html.hc.html.forms.HCSelect;
import com.helger.phoss.smp.domain.SMPMetaManager;
import com.helger.phoss.smp.domain.accesspoint.ISMPAccessPoint;
import com.helger.photon.core.form.RequestField;

/**
 * Select for all existing Access Points. The first entry is always the "no Access Point" entry,
 * because referencing an Access Point is an opt-in feature per endpoint.
 *
 * @author Philip Helger
 * @since 8.4.4
 */
public class HCSMPAccessPointSelect extends HCSelect
{
  public static final String VALUE_NONE = "";

  @NonNull
  @Nonempty
  public static String getDisplayName (@NonNull final ISMPAccessPoint aAP)
  {
    return StringHelper.isNotEmpty (aAP.getEndpointReference ()) ? aAP.getName () +
                                                                   " (" +
                                                                   aAP.getEndpointReference () +
                                                                   ")" : aAP.getName ();
  }

  public HCSMPAccessPointSelect (@NonNull final RequestField aRF)
  {
    super (aRF);

    addOption (VALUE_NONE, "- none - (use the data below)");
    for (final ISMPAccessPoint aAP : SMPMetaManager.getAccessPointMgr ()
                                                   .getAllAccessPoints ()
                                                   .getSortedInline (ISMPAccessPoint.comparator ()))
      addOption (aAP.getID (), getDisplayName (aAP));
  }
}
