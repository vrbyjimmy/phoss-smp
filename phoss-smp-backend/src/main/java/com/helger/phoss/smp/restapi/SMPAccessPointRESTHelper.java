/*
 * Copyright (C) 2015-2026 Philip Helger and contributors
 * philip[at]helger[dot]com
 *
 * The Original Code is Copyright The Peppol project (http://www.peppol.eu)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.helger.phoss.smp.restapi;

import java.net.URI;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.annotation.concurrent.Immutable;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.phoss.smp.domain.SMPMetaManager;
import com.helger.phoss.smp.domain.accesspoint.ISMPAccessPoint;
import com.helger.phoss.smp.domain.accesspoint.SMPAccessPointHelper;
import com.helger.phoss.smp.domain.serviceinfo.SMPEndpoint;
import com.helger.phoss.smp.exception.SMPBadRequestException;

/**
 * Helper class to handle the referencing of Access Points via the REST API.
 * <p>
 * An endpoint that is written via the REST API may either contain the endpoint reference URL and
 * the certificate directly (the classic way) or it may reference an Access Point by name. The
 * latter is expressed by using the value <code>accesspoint:<em>name</em></code> as the endpoint
 * reference URL and by not providing a certificate.
 * <p>
 * When reading data via the REST API, the endpoint reference URL and the certificate of the
 * referenced Access Point are always returned, so that the result stays compliant with the
 * respective SMP specification.
 *
 * @author Philip Helger
 * @since 8.4.4
 */
@Immutable
public final class SMPAccessPointRESTHelper
{
  private SMPAccessPointRESTHelper ()
  {}

  /**
   * Check if the endpoint reference URL of the provided endpoint references an Access Point by
   * name. If it does, the Access Point is resolved and assigned to the endpoint. If it does not,
   * the endpoint is left unchanged and keeps its direct data.
   *
   * @param aEndpoint
   *        The endpoint to be handled. May not be <code>null</code>.
   * @param aEffectedURI
   *        The URI to be used in case of an error. May be <code>null</code>.
   * @throws SMPBadRequestException
   *         If the referenced Access Point does not exist or if the endpoint additionally contains
   *         a certificate.
   */
  public static void applyAccessPointReference (@NonNull final SMPEndpoint aEndpoint,
                                                @Nullable final URI aEffectedURI) throws SMPBadRequestException
  {
    ValueEnforcer.notNull (aEndpoint, "Endpoint");

    final String sAccessPointName = SMPAccessPointHelper.getAccessPointNameFromRESTReference (aEndpoint.getEndpointReference ());
    if (sAccessPointName == null)
    {
      // Regular endpoint with direct data
      return;
    }

    if (aEndpoint.hasCertificate ())
      throw new SMPBadRequestException ("The Endpoint references the Access Point '" +
                                        sAccessPointName +
                                        "' and must therefore not contain a certificate",
                                        aEffectedURI);

    final ISMPAccessPoint aAccessPoint = SMPMetaManager.getAccessPointMgr ().getAccessPointOfName (sAccessPointName);
    if (aAccessPoint == null)
      throw new SMPBadRequestException ("The Endpoint references the Access Point '" +
                                        sAccessPointName +
                                        "' but no such Access Point exists",
                                        aEffectedURI);

    aEndpoint.setAccessPoint (aAccessPoint);
  }
}
