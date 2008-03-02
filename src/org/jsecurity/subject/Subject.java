/*
 * Copyright (C) 2005-2007 Les Hazlewood, Jeremy Haile
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General
 * Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the
 *
 * Free Software Foundation, Inc.
 * 59 Temple Place, Suite 330
 * Boston, MA 02111-1307
 * USA
 *
 * Or, you may view it online at
 * http://www.opensource.org/licenses/lgpl-license.php
 */
package org.jsecurity.subject;

import org.jsecurity.authc.AuthenticationException;
import org.jsecurity.authc.AuthenticationToken;
import org.jsecurity.authz.AuthorizationException;
import org.jsecurity.authz.Permission;
import org.jsecurity.session.Session;

import java.util.Collection;
import java.util.List;

/**
 * A <tt>Subject</tt> represents state and security operations for a <em>single</em> application user.
 * These operations include authentication (login/logout), authorization (access control), and
 * session functionality. It is JSecurity's primary mechanism for single-user security functionality.
 *
 * @since 0.1
 * @author Les Hazlewood
 * @author Jeremy Haile
 */
public interface Subject {

    /**
     * Returns this Subject's uniquely-identifying principal, or <tt>null</tt> if this
     * Subject doesn't yet have account data associated with it (for example, if they haven't logged in).
     *
     * <p>The term <em>principal</em> is just a fancy security term for any identifying attribute(s) of an application
     * user, such as a username, or user id, or public key, or anything else you might use in your application to
     * identify a user.  And although given names and family names (first/last) are technically principals as well,
     * JSecurity expects the object(s) returned from this method to be uniquely identifying attibute(s) for
     * your application.  This implies that things like given names and family names are usually poor candidates as
     * return values since they are rarely guaranteed to be unique.</p>
     *
     * <p>Most single-Realm applications would return from this method a single unique principal as noted above
     * (for example a String username or Long user id, etc, etc).  Single-realm applications represent the large 
     * majority of JSecurity applications.</p>
     *
     * <p>However, in <em>multi</em>-Realm configurations, which are fully supported by JSecurity as well, it is
     * possible that the return value encapsulates more than one principal.  Typically multi-realm applications need to
     * retain the unique principals for <em>each</em> Realm so subsequent security checks against these Realms can
     * utilize these multiple principals.  In these cases, the object returned could be a Collection or any
     * application-specific instance that encapsulates the principals.</p>
     *
     * @return this Subject's application-specific identity.
     */
    Object getPrincipal();

    /**
     * Returns a single principal assignable from the specified type, or <tt>null</tt> if there are none of the
     * specified type.
     *
     * <p>If multiple principals of this type are associated with this Subject, it is up to the specific implementation
     * as to which principal will be returned.
     *
     * @param principalType the type of the principal that should be returned.
     * @return a principal of the specified type.
     */
    <T> T getPrincipalByType(Class<T> principalType);

    /**
     * Returns all principals assignable from the specified type that is associated with this <tt>Subject</tt>, or an
     * empty Collection if no principals are associated.
     *
     * @param principalType the principal type that should be returned.
     * @return a Collection of principals that are assignable from the specified type, or
     * an empty Collection if no principals of this type are associated.
     */
    <T> Collection<T> getAllPrincipalsByType(Class<T> principalType);

    /**
     * Returns <tt>true</tt> if this Subject is
     * permitted to perform an action or access a resource summarized by the specified permission.
     *
     * @param permission the permission that is being checked.
     * @return true if the user associated with this Subject is permitted, false otherwise.
     * @since 0.9
     */
    boolean isPermitted( String permission );

    /**
     * Returns <tt>true</tt> if this Subject is
     * permitted to perform an action or access a resource summarized by the specified permission.
     *
     * @param permission the permission that is being checked.
     * @return true if the user associated with this Subject is permitted, false otherwise.
     */
    boolean isPermitted( Permission permission );

    /**
     * Checks a collection of permissions to see if this Subject is permitted any of the specified permissions, and
     * and returns a boolean array indicating which ones are permitted.
     *
     * @param permissions the permissions to check for.
     * @return an array of booleans whose indices correspond to the index of the
     * permissions in the given list.  A true value at an index indicates the Subject is permitted for
     * for the associated <tt>Permission</tt> object in the list.  A false value at an index
     * indicates otherwise.
     * @since 0.9
     */
    boolean[] isPermitted( String... permissions );

    /**
     * Checks a collection of permissions to see if this Subject is permitted any of the specified permissions, and
     * and returns a boolean array indicating which ones are permitted.
     *
     * @param permissions the permissions to check for.
     * @return an array of booleans whose indices correspond to the index of the
     * permissions in the given list.  A true value at an index indicates the Subject is permitted for
     * for the associated <tt>Permission</tt> object in the list.  A false value at an index
     * indicates otherwise.
     */
    boolean[] isPermitted( List<Permission> permissions );

    /**
     * Returns <tt>true</tt> if the Subject has all of the given permissions, <tt>false</tt> otherwise.
     * @param permissions the permissions to be checked.
     * @return <tt>true</tt> if the Subject has all of the given permissions, <tt>false</tt> otherwise.
     * @since 0.9
     */
    boolean isPermittedAll( String... permissions );

    /**
     * Returns <tt>true</tt> if the Subject has all of the given permissions, <tt>false</tt> otherwise.
     * @param permissions the permissions to be checked.
     * @return <tt>true</tt> if the Subject has all of the given permissions, <tt>false</tt> otherwise.
     */
    boolean isPermittedAll( Collection<Permission> permissions );

    /**
     * A convenience method to check if this Subject isPermitted the specified permission.
     * If the Subject does not imply the given permission, an
     * {@link org.jsecurity.authz.AuthorizationException} will be thrown.
     * @param permission the permission to check.
     * @throws org.jsecurity.authz.AuthorizationException if the user does not have the permission.
     * @since 0.9
     */
    void checkPermission( String permission ) throws AuthorizationException;

    /**
     * A convenience method to check if this Subject isPermitted the specified permission.
     * If the Subject does not imply the given permission, an
     * {@link org.jsecurity.authz.AuthorizationException} will be thrown.
     * @param permission the permission to check.
     * @throws org.jsecurity.authz.AuthorizationException if the user does not have the permission.
     */
    void checkPermission( Permission permission ) throws AuthorizationException;

    /**
     * A convenience method for checking if this Subject isPermitted all of the specified permissions.
     * If the Subject does not imply all of the given permissions, an
     * {@link org.jsecurity.authz.AuthorizationException} will be thrown.
     * @param permissions the permissions to check.
     * @throws AuthorizationException if the Subject does not imply all of the given permissions.
     * @since 0.9
     */
    void checkPermissions( String... permissions ) throws AuthorizationException;

    /**
     * A convenience method for checking if this Subject isPermitted all of the specified permissions.
     * If the Subject does not imply all of the given permissions, an
     * {@link org.jsecurity.authz.AuthorizationException} will be thrown.
     * @param permissions the permissions to check.
     * @throws AuthorizationException if the Subject does not imply all of the given permissions.
     */
    void checkPermissions( Collection<Permission> permissions ) throws AuthorizationException;

    /**
     * Returns <tt>true</tt> if this Subject has the given role, <tt>false</tt> otherwise.
     * @param role the role identifier that is being checked.
     * @return <tt>true</tt> if this Subject has the given role, <tt>false</tt> otherwise.
     */
    boolean hasRole( String role );

    /**
     * Checks a set of role identifiers to see if they are associated with this
     * Subject and returns a boolean array indicating which roles are associated.
     *
     * <p>This is primarily a performance-enhancing method to help reduce the number of
     * {@link #hasRole} invocations over the wire in client/server systems.
     *
     * @param roles the role identifiers to check for.
     * @return an array of booleans whose indices correspond to the index of the
     * roles in the given identifiers.  A true value indicates the user has the
     * role at that index.  False indicates the user does not have the role.
     */
    boolean[] hasRoles( List<String> roles);

    /**
     * Checks if the user has all of the given roles.
     * @param roles the roles to be checked.
     * @return true if the user has all roles, false otherwise.
     */
    boolean hasAllRoles( Collection<String> roles );

    /**
     * A convenience method to check if the given role is associated with this Subject.
     * If the Subject does not imply the given role, an
     * {@link org.jsecurity.authz.AuthorizationException} will be thrown.
     * @param role the role identifier to check.
     * @throws org.jsecurity.authz.AuthorizationException if the user does not have the role.
     */
    void checkRole( String role ) throws AuthorizationException;

    /**
     * A convenience method for checking if this Subject has all of the given roles.
     * If the Subject does not have them all, an {@link org.jsecurity.authz.AuthorizationException} will be
     * thrown.
     * @param roles the roles to check..
     * @throws AuthorizationException if the Subject does not have all of the given roles.
     */
    void checkRoles( Collection<String> roles ) throws AuthorizationException;

    /**
     * Performs a login attempt for the Subject associated with the calling code.  If unsuccessful,
     * an {@link AuthenticationException} is thrown, the subclass of which identifies why the attempt failed.
     * If successful, the account data associated with the submitted principals/credentials will be
     * associated with this <tt>Subject</tt> and the method will return quietly.
     *
     * <p>Upon returninq quietly, this <tt>Subject</tt> instance can be considered
     * authenticated and {@link #getPrincipal() getPrincipal()} will be non-null and
     * {@link #isAuthenticated() isAuthenticated()} will be <tt>true</tt>.
     *
     * @param token the token encapsulating the subject's principals and credentials to be passed to the
     * Authentication subsystem for verification.
     * @throws AuthenticationException if the authentication attempt fails.
     *
     * @since 0.9
     */
    void login( AuthenticationToken token ) throws AuthenticationException;

    /**
     * Returns <tt>true</tt> if this Subject (user) has proven their identity
     * by providing valid credentials matching those known to the system, <tt>false</tt> otherwise.
     * 
     * <p>Note that even if this Subject's identity has been remembered via 'remember me' services, this method will
     * still return <tt>false</tt> unless the user has actually logged in with proper credentials.  See the
     * {@link org.jsecurity.authc.RememberMeAuthenticationToken RememberMeAuthenticationToken} class JavaDoc for why
     * this would occur.</p>
     *
     * @return <tt>true</tt> if this Subject has proven their identity
     * by providing valid credentials matching those known to the system, <tt>false</tt> otherwise.
     *
     * @since 0.9
     */
    boolean isAuthenticated();

    /**
     * Returns the application <tt>Session</tt> associated with this Subject.  If no session exists when this
     * method is called, a new session will be created, associated with this Subject, and then returned.
     * 
     * @see #getSession(boolean)
     *
     * @return the application <tt>Session</tt> associated with this Subject.
     *
     * @since 0.2
     */
    Session getSession();

    /**
     * Returns the application <tt>Session</tt> associated with this Subject.  Based on the boolean argument,
     * this method functions as follows:
     *
     * <ul>
     *   <li>If there is already an existing session associated with this <tt>Subject</tt>, it is returned and
     * the <tt>create</tt> argument is ignored.</li>
     *   <li>If no session exists and <tt>create</tt> is <tt>true</tt>, a new session will be created, associated with
     * this <tt>Subject</tt> and then returned.</li>
     *   <li>If no session exists and <tt>create</tt> is <tt>false</tt>, <tt>null</tt> is returned.</li>
     * </ul>
     *
     * @param create boolean argument determining if a new session should be created or not if there is no existing session.
     * @return the application <tt>Session</tt> associated with this <tt>Subject</tt> or <tt>null</tt> based
     * on the above described logic.
     *
     * @since 0.2
     */
    Session getSession( boolean create );

    /**
     * Logs out this Subject and invalidates and/or removes any associated entities
     * (such as a {@link Session Session} and authorization data.
     */
    void logout();

}
