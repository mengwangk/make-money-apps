/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: C:\\MySpace\\2011\\workspace\\make-money-apps\\SMSSpeaker\\src\\com\\android\\internal\\telephony\\ITelephony.aidl
 */
package com.android.internal.telephony;
//import android.telephony.NeighboringCellInfo;
/**
 * Interface used to interact with the phone.  Mostly this is used by the
 * TelephonyManager class.  A few places are still using this directly.
 * Please clean them up if possible and use TelephonyManager insteadl.
 *
 * {@hide}
 */
public interface ITelephony extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.android.internal.telephony.ITelephony
{
private static final java.lang.String DESCRIPTOR = "com.android.internal.telephony.ITelephony";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.android.internal.telephony.ITelephony interface,
 * generating a proxy if needed.
 */
public static com.android.internal.telephony.ITelephony asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.android.internal.telephony.ITelephony))) {
return ((com.android.internal.telephony.ITelephony)iin);
}
return new com.android.internal.telephony.ITelephony.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_dial:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.dial(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_call:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.call(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_showCallScreen:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.showCallScreen();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_showCallScreenWithDialpad:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
boolean _result = this.showCallScreenWithDialpad(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_endCall:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.endCall();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_answerRingingCall:
{
data.enforceInterface(DESCRIPTOR);
this.answerRingingCall();
reply.writeNoException();
return true;
}
case TRANSACTION_silenceRinger:
{
data.enforceInterface(DESCRIPTOR);
this.silenceRinger();
reply.writeNoException();
return true;
}
case TRANSACTION_isOffhook:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isOffhook();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_isRinging:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isRinging();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_isIdle:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isIdle();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_isRadioOn:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isRadioOn();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_isSimPinEnabled:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isSimPinEnabled();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_isSimFDNEnabled:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isSimFDNEnabled();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_cancelMissedCallsNotification:
{
data.enforceInterface(DESCRIPTOR);
this.cancelMissedCallsNotification();
reply.writeNoException();
return true;
}
case TRANSACTION_supplyPin:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _result = this.supplyPin(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_supplyPuk:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
boolean _result = this.supplyPuk(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_supplyPin2:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _result = this.supplyPin2(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getMsisdnavailable:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.getMsisdnavailable();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_handlePinMmi:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _result = this.handlePinMmi(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_toggleRadioOnOff:
{
data.enforceInterface(DESCRIPTOR);
this.toggleRadioOnOff();
reply.writeNoException();
return true;
}
case TRANSACTION_setRadio:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
boolean _result = this.setRadio(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_updateServiceLocation:
{
data.enforceInterface(DESCRIPTOR);
this.updateServiceLocation();
reply.writeNoException();
return true;
}
case TRANSACTION_enableLocationUpdates:
{
data.enforceInterface(DESCRIPTOR);
this.enableLocationUpdates();
reply.writeNoException();
return true;
}
case TRANSACTION_disableLocationUpdates:
{
data.enforceInterface(DESCRIPTOR);
this.disableLocationUpdates();
reply.writeNoException();
return true;
}
case TRANSACTION_enableApnType:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _result = this.enableApnType(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_disableApnType:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _result = this.disableApnType(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_enableDataConnectivity:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.enableDataConnectivity();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_disableDataConnectivity:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.disableDataConnectivity();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_isDataConnectivityPossible:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isDataConnectivityPossible();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getCellLocation:
{
data.enforceInterface(DESCRIPTOR);
android.os.Bundle _result = this.getCellLocation();
reply.writeNoException();
if ((_result!=null)) {
reply.writeInt(1);
_result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_getCallState:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getCallState();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getDataActivity:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getDataActivity();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getDataState:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getDataState();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getActivePhoneType:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getActivePhoneType();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getCdmaEriIconIndex:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getCdmaEriIconIndex();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getCdmaEriIconMode:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getCdmaEriIconMode();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getCdmaEriText:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getCdmaEriText();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getCdmaNeedsProvisioning:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.getCdmaNeedsProvisioning();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getVoiceMessageCount:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getVoiceMessageCount();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getNetworkType:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getNetworkType();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_hasIccCard:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.hasIccCard();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_initiateFakecall:
{
data.enforceInterface(DESCRIPTOR);
this.initiateFakecall();
reply.writeNoException();
return true;
}
case TRANSACTION_getSimPinRetry:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getSimPinRetry();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getSimPukRetry:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getSimPukRetry();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_switchHoldingAndActive:
{
data.enforceInterface(DESCRIPTOR);
this.switchHoldingAndActive();
reply.writeNoException();
return true;
}
case TRANSACTION_setMute:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
this.setMute(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getMute:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.getMute();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_turnOnSpeaker:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
this.turnOnSpeaker(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getActiveCallsCount:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getActiveCallsCount();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getHoldCallsCount:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getHoldCallsCount();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getCallerName:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getCallerName();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getCallTime:
{
data.enforceInterface(DESCRIPTOR);
long _result = this.getCallTime();
reply.writeNoException();
reply.writeLong(_result);
return true;
}
case TRANSACTION_getCallBaseTime:
{
data.enforceInterface(DESCRIPTOR);
long _result = this.getCallBaseTime();
reply.writeNoException();
reply.writeLong(_result);
return true;
}
case TRANSACTION_isVideoCall:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isVideoCall();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getDataStatebyApnType:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _result = this.getDataStatebyApnType(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.android.internal.telephony.ITelephony
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
/**
     * Dial a number. This doesn't place the call. It displays
     * the Dialer screen.
     * @param number the number to be dialed. If null, this
     * would display the Dialer screen with no number pre-filled.
     */
public void dial(java.lang.String number) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(number);
mRemote.transact(Stub.TRANSACTION_dial, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * Place a call to the specified number.
     * @param number the number to be called.
     */
public void call(java.lang.String number) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(number);
mRemote.transact(Stub.TRANSACTION_call, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * If there is currently a call in progress, show the call screen.
     * The DTMF dialpad may or may not be visible initially, depending on
     * whether it was up when the user last exited the InCallScreen.
     *
     * @return true if the call screen was shown.
     */
public boolean showCallScreen() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_showCallScreen, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Variation of showCallScreen() that also specifies whether the
     * DTMF dialpad should be initially visible when the InCallScreen
     * comes up.
     *
     * @param showDialpad if true, make the dialpad visible initially,
     *                    otherwise hide the dialpad initially.
     * @return true if the call screen was shown.
     *
     * @see showCallScreen
     */
public boolean showCallScreenWithDialpad(boolean showDialpad) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((showDialpad)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_showCallScreenWithDialpad, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * End call or go to the Home screen
     *
     * @return whether it hung up
     */
public boolean endCall() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_endCall, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Answer the currently-ringing call.
     *
     * If there's already a current active call, that call will be
     * automatically put on hold.  If both lines are currently in use, the
     * current active call will be ended.
     *
     * TODO: provide a flag to let the caller specify what policy to use
     * if both lines are in use.  (The current behavior is hardwired to
     * "answer incoming, end ongoing", which is how the CALL button
     * is specced to behave.)
     *
     * TODO: this should be a oneway call (especially since it's called
     * directly from the key queue thread).
     */
public void answerRingingCall() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_answerRingingCall, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * Silence the ringer if an incoming call is currently ringing.
     * (If vibrating, stop the vibrator also.)
     *
     * It's safe to call this if the ringer has already been silenced, or
     * even if there's no incoming call.  (If so, this method will do nothing.)
     *
     * TODO: this should be a oneway call too (see above).
     *       (Actually *all* the methods here that return void can
     *       probably be oneway.)
     */
public void silenceRinger() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_silenceRinger, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * Check if we are in either an active or holding call
     * @return true if the phone state is OFFHOOK.
     */
public boolean isOffhook() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isOffhook, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Check if an incoming phone call is ringing or call waiting.
     * @return true if the phone state is RINGING.
     */
public boolean isRinging() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isRinging, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Check if the phone is idle.
     * @return true if the phone state is IDLE.
     */
public boolean isIdle() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isIdle, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Check to see if the radio is on or not.
     * @return returns true if the radio is on.
     */
public boolean isRadioOn() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isRadioOn, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Check if the SIM pin lock is enabled.
     * @return true if the SIM pin lock is enabled.
     */
public boolean isSimPinEnabled() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isSimPinEnabled, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Check if the SIM FDN lock is enabled.
     * @return true if the SIM FDN lock is enabled.
     */
public boolean isSimFDNEnabled() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isSimFDNEnabled, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Cancels the missed calls notification.
     */
public void cancelMissedCallsNotification() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_cancelMissedCallsNotification, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * Supply a pin to unlock the SIM.  Blocks until a result is determined.
     * @param pin The pin to check.
     * @return whether the operation was a success.
     */
public boolean supplyPin(java.lang.String pin) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(pin);
mRemote.transact(Stub.TRANSACTION_supplyPin, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Supply a pin to unlock the SIM.  Blocks until a result is determined.
     * @param pin The pin to check.
     * @return whether the operation was a success.
     */
public boolean supplyPuk(java.lang.String pin, java.lang.String newPin) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(pin);
_data.writeString(newPin);
mRemote.transact(Stub.TRANSACTION_supplyPuk, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Supply a pin2 to unlock the SIM.  Blocks until a result is determined.
     * @param pin The pin2 to check.
     * @return whether the operation was a success.
     */
public boolean supplyPin2(java.lang.String pin2) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(pin2);
mRemote.transact(Stub.TRANSACTION_supplyPin2, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Check MS-ISDN field in SIM.
     * @param 
     * @return whether there is MS-ISDN field in SIM.
     */
public boolean getMsisdnavailable() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getMsisdnavailable, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Handles PIN MMI commands (PIN/PIN2/PUK/PUK2), which are initiated
     * without SEND (so <code>dial</code> is not appropriate).
     *
     * @param dialString the MMI command to be executed.
     * @return true if MMI command is executed.
     */
public boolean handlePinMmi(java.lang.String dialString) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(dialString);
mRemote.transact(Stub.TRANSACTION_handlePinMmi, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Toggles the radio on or off.
     */
public void toggleRadioOnOff() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_toggleRadioOnOff, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * Set the radio to on or off
     */
public boolean setRadio(boolean turnOn) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((turnOn)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_setRadio, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Request to update location information in service state
     */
public void updateServiceLocation() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_updateServiceLocation, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * Enable location update notifications.
     */
public void enableLocationUpdates() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_enableLocationUpdates, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * Disable location update notifications.
     */
public void disableLocationUpdates() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_disableLocationUpdates, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * Enable a specific APN type.
     */
public int enableApnType(java.lang.String type) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(type);
mRemote.transact(Stub.TRANSACTION_enableApnType, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Disable a specific APN type.
     */
public int disableApnType(java.lang.String type) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(type);
mRemote.transact(Stub.TRANSACTION_disableApnType, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Allow mobile data connections.
     */
public boolean enableDataConnectivity() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_enableDataConnectivity, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Disallow mobile data connections.
     */
public boolean disableDataConnectivity() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_disableDataConnectivity, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Report whether data connectivity is possible.
     */
public boolean isDataConnectivityPossible() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isDataConnectivityPossible, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public android.os.Bundle getCellLocation() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
android.os.Bundle _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getCellLocation, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = android.os.Bundle.CREATOR.createFromParcel(_reply);
}
else {
_result = null;
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Returns the neighboring cell information of the device.
     *///List<NeighboringCellInfo> getNeighboringCellInfo();

public int getCallState() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getCallState, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public int getDataActivity() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getDataActivity, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public int getDataState() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getDataState, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Returns the current active phone type as integer.
     * Returns TelephonyManager.PHONE_TYPE_CDMA if RILConstants.CDMA_PHONE
     * and TelephonyManager.PHONE_TYPE_GSM if RILConstants.GSM_PHONE
     */
public int getActivePhoneType() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getActivePhoneType, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Returns the CDMA ERI icon index to display
     */
public int getCdmaEriIconIndex() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getCdmaEriIconIndex, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Returns the CDMA ERI icon mode,
     * 0 - ON
     * 1 - FLASHING
     */
public int getCdmaEriIconMode() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getCdmaEriIconMode, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Returns the CDMA ERI text,
     */
public java.lang.String getCdmaEriText() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getCdmaEriText, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Returns true if CDMA provisioning needs to run.
     */
public boolean getCdmaNeedsProvisioning() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getCdmaNeedsProvisioning, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
      * Returns the unread count of voicemails
      */
public int getVoiceMessageCount() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getVoiceMessageCount, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
      * Returns the network type
      */
public int getNetworkType() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getNetworkType, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Return true if an ICC card is present
     */
public boolean hasIccCard() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_hasIccCard, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
//[ TelephonyFeature.CONFIG_FAKECALL_FEATURE

public void initiateFakecall() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_initiateFakecall, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//]
/**
     * Returns the Value of PIN remain retry number.
     */
public int getSimPinRetry() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getSimPinRetry, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Returns the Value of PUK remain retry number.
     */
public int getSimPukRetry() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getSimPukRetry, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Switch Holding and Active
     */
public void switchHoldingAndActive() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_switchHoldingAndActive, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * Make call mute or unmute
     */
public void setMute(boolean flag) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((flag)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_setMute, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * Return mute state
     */
public boolean getMute() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getMute, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Make speaker call on or off
     */
public void turnOnSpeaker(boolean flag) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((flag)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_turnOnSpeaker, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * Return Active calls count.
     */
public int getActiveCallsCount() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getActiveCallsCount, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Return Hold calls count.
     */
public int getHoldCallsCount() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getHoldCallsCount, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Return Caller name
     * First active call has priority
     */
public java.lang.String getCallerName() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getCallerName, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Return Call time
     * First active call has priority
     */
public long getCallTime() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
long _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getCallTime, _data, _reply, 0);
_reply.readException();
_result = _reply.readLong();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Return Call base time
     * First active call has priority
     */
public long getCallBaseTime() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
long _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getCallBaseTime, _data, _reply, 0);
_reply.readException();
_result = _reply.readLong();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Return TRUE, if current call is video call
     * First active call has priority
     */
public boolean isVideoCall() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isVideoCall, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
// [TelephonyFeature.CONFIG_MULTIPLE_PDP_FEATURE

public boolean getDataStatebyApnType(java.lang.String ApnType) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(ApnType);
mRemote.transact(Stub.TRANSACTION_getDataStatebyApnType, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_dial = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_call = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_showCallScreen = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_showCallScreenWithDialpad = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_endCall = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_answerRingingCall = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_silenceRinger = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_isOffhook = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_isRinging = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_isIdle = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_isRadioOn = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_isSimPinEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_isSimFDNEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
static final int TRANSACTION_cancelMissedCallsNotification = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
static final int TRANSACTION_supplyPin = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
static final int TRANSACTION_supplyPuk = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
static final int TRANSACTION_supplyPin2 = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
static final int TRANSACTION_getMsisdnavailable = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
static final int TRANSACTION_handlePinMmi = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
static final int TRANSACTION_toggleRadioOnOff = (android.os.IBinder.FIRST_CALL_TRANSACTION + 19);
static final int TRANSACTION_setRadio = (android.os.IBinder.FIRST_CALL_TRANSACTION + 20);
static final int TRANSACTION_updateServiceLocation = (android.os.IBinder.FIRST_CALL_TRANSACTION + 21);
static final int TRANSACTION_enableLocationUpdates = (android.os.IBinder.FIRST_CALL_TRANSACTION + 22);
static final int TRANSACTION_disableLocationUpdates = (android.os.IBinder.FIRST_CALL_TRANSACTION + 23);
static final int TRANSACTION_enableApnType = (android.os.IBinder.FIRST_CALL_TRANSACTION + 24);
static final int TRANSACTION_disableApnType = (android.os.IBinder.FIRST_CALL_TRANSACTION + 25);
static final int TRANSACTION_enableDataConnectivity = (android.os.IBinder.FIRST_CALL_TRANSACTION + 26);
static final int TRANSACTION_disableDataConnectivity = (android.os.IBinder.FIRST_CALL_TRANSACTION + 27);
static final int TRANSACTION_isDataConnectivityPossible = (android.os.IBinder.FIRST_CALL_TRANSACTION + 28);
static final int TRANSACTION_getCellLocation = (android.os.IBinder.FIRST_CALL_TRANSACTION + 29);
static final int TRANSACTION_getCallState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 30);
static final int TRANSACTION_getDataActivity = (android.os.IBinder.FIRST_CALL_TRANSACTION + 31);
static final int TRANSACTION_getDataState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 32);
static final int TRANSACTION_getActivePhoneType = (android.os.IBinder.FIRST_CALL_TRANSACTION + 33);
static final int TRANSACTION_getCdmaEriIconIndex = (android.os.IBinder.FIRST_CALL_TRANSACTION + 34);
static final int TRANSACTION_getCdmaEriIconMode = (android.os.IBinder.FIRST_CALL_TRANSACTION + 35);
static final int TRANSACTION_getCdmaEriText = (android.os.IBinder.FIRST_CALL_TRANSACTION + 36);
static final int TRANSACTION_getCdmaNeedsProvisioning = (android.os.IBinder.FIRST_CALL_TRANSACTION + 37);
static final int TRANSACTION_getVoiceMessageCount = (android.os.IBinder.FIRST_CALL_TRANSACTION + 38);
static final int TRANSACTION_getNetworkType = (android.os.IBinder.FIRST_CALL_TRANSACTION + 39);
static final int TRANSACTION_hasIccCard = (android.os.IBinder.FIRST_CALL_TRANSACTION + 40);
static final int TRANSACTION_initiateFakecall = (android.os.IBinder.FIRST_CALL_TRANSACTION + 41);
static final int TRANSACTION_getSimPinRetry = (android.os.IBinder.FIRST_CALL_TRANSACTION + 42);
static final int TRANSACTION_getSimPukRetry = (android.os.IBinder.FIRST_CALL_TRANSACTION + 43);
static final int TRANSACTION_switchHoldingAndActive = (android.os.IBinder.FIRST_CALL_TRANSACTION + 44);
static final int TRANSACTION_setMute = (android.os.IBinder.FIRST_CALL_TRANSACTION + 45);
static final int TRANSACTION_getMute = (android.os.IBinder.FIRST_CALL_TRANSACTION + 46);
static final int TRANSACTION_turnOnSpeaker = (android.os.IBinder.FIRST_CALL_TRANSACTION + 47);
static final int TRANSACTION_getActiveCallsCount = (android.os.IBinder.FIRST_CALL_TRANSACTION + 48);
static final int TRANSACTION_getHoldCallsCount = (android.os.IBinder.FIRST_CALL_TRANSACTION + 49);
static final int TRANSACTION_getCallerName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 50);
static final int TRANSACTION_getCallTime = (android.os.IBinder.FIRST_CALL_TRANSACTION + 51);
static final int TRANSACTION_getCallBaseTime = (android.os.IBinder.FIRST_CALL_TRANSACTION + 52);
static final int TRANSACTION_isVideoCall = (android.os.IBinder.FIRST_CALL_TRANSACTION + 53);
static final int TRANSACTION_getDataStatebyApnType = (android.os.IBinder.FIRST_CALL_TRANSACTION + 54);
}
/**
     * Dial a number. This doesn't place the call. It displays
     * the Dialer screen.
     * @param number the number to be dialed. If null, this
     * would display the Dialer screen with no number pre-filled.
     */
public void dial(java.lang.String number) throws android.os.RemoteException;
/**
     * Place a call to the specified number.
     * @param number the number to be called.
     */
public void call(java.lang.String number) throws android.os.RemoteException;
/**
     * If there is currently a call in progress, show the call screen.
     * The DTMF dialpad may or may not be visible initially, depending on
     * whether it was up when the user last exited the InCallScreen.
     *
     * @return true if the call screen was shown.
     */
public boolean showCallScreen() throws android.os.RemoteException;
/**
     * Variation of showCallScreen() that also specifies whether the
     * DTMF dialpad should be initially visible when the InCallScreen
     * comes up.
     *
     * @param showDialpad if true, make the dialpad visible initially,
     *                    otherwise hide the dialpad initially.
     * @return true if the call screen was shown.
     *
     * @see showCallScreen
     */
public boolean showCallScreenWithDialpad(boolean showDialpad) throws android.os.RemoteException;
/**
     * End call or go to the Home screen
     *
     * @return whether it hung up
     */
public boolean endCall() throws android.os.RemoteException;
/**
     * Answer the currently-ringing call.
     *
     * If there's already a current active call, that call will be
     * automatically put on hold.  If both lines are currently in use, the
     * current active call will be ended.
     *
     * TODO: provide a flag to let the caller specify what policy to use
     * if both lines are in use.  (The current behavior is hardwired to
     * "answer incoming, end ongoing", which is how the CALL button
     * is specced to behave.)
     *
     * TODO: this should be a oneway call (especially since it's called
     * directly from the key queue thread).
     */
public void answerRingingCall() throws android.os.RemoteException;
/**
     * Silence the ringer if an incoming call is currently ringing.
     * (If vibrating, stop the vibrator also.)
     *
     * It's safe to call this if the ringer has already been silenced, or
     * even if there's no incoming call.  (If so, this method will do nothing.)
     *
     * TODO: this should be a oneway call too (see above).
     *       (Actually *all* the methods here that return void can
     *       probably be oneway.)
     */
public void silenceRinger() throws android.os.RemoteException;
/**
     * Check if we are in either an active or holding call
     * @return true if the phone state is OFFHOOK.
     */
public boolean isOffhook() throws android.os.RemoteException;
/**
     * Check if an incoming phone call is ringing or call waiting.
     * @return true if the phone state is RINGING.
     */
public boolean isRinging() throws android.os.RemoteException;
/**
     * Check if the phone is idle.
     * @return true if the phone state is IDLE.
     */
public boolean isIdle() throws android.os.RemoteException;
/**
     * Check to see if the radio is on or not.
     * @return returns true if the radio is on.
     */
public boolean isRadioOn() throws android.os.RemoteException;
/**
     * Check if the SIM pin lock is enabled.
     * @return true if the SIM pin lock is enabled.
     */
public boolean isSimPinEnabled() throws android.os.RemoteException;
/**
     * Check if the SIM FDN lock is enabled.
     * @return true if the SIM FDN lock is enabled.
     */
public boolean isSimFDNEnabled() throws android.os.RemoteException;
/**
     * Cancels the missed calls notification.
     */
public void cancelMissedCallsNotification() throws android.os.RemoteException;
/**
     * Supply a pin to unlock the SIM.  Blocks until a result is determined.
     * @param pin The pin to check.
     * @return whether the operation was a success.
     */
public boolean supplyPin(java.lang.String pin) throws android.os.RemoteException;
/**
     * Supply a pin to unlock the SIM.  Blocks until a result is determined.
     * @param pin The pin to check.
     * @return whether the operation was a success.
     */
public boolean supplyPuk(java.lang.String pin, java.lang.String newPin) throws android.os.RemoteException;
/**
     * Supply a pin2 to unlock the SIM.  Blocks until a result is determined.
     * @param pin The pin2 to check.
     * @return whether the operation was a success.
     */
public boolean supplyPin2(java.lang.String pin2) throws android.os.RemoteException;
/**
     * Check MS-ISDN field in SIM.
     * @param 
     * @return whether there is MS-ISDN field in SIM.
     */
public boolean getMsisdnavailable() throws android.os.RemoteException;
/**
     * Handles PIN MMI commands (PIN/PIN2/PUK/PUK2), which are initiated
     * without SEND (so <code>dial</code> is not appropriate).
     *
     * @param dialString the MMI command to be executed.
     * @return true if MMI command is executed.
     */
public boolean handlePinMmi(java.lang.String dialString) throws android.os.RemoteException;
/**
     * Toggles the radio on or off.
     */
public void toggleRadioOnOff() throws android.os.RemoteException;
/**
     * Set the radio to on or off
     */
public boolean setRadio(boolean turnOn) throws android.os.RemoteException;
/**
     * Request to update location information in service state
     */
public void updateServiceLocation() throws android.os.RemoteException;
/**
     * Enable location update notifications.
     */
public void enableLocationUpdates() throws android.os.RemoteException;
/**
     * Disable location update notifications.
     */
public void disableLocationUpdates() throws android.os.RemoteException;
/**
     * Enable a specific APN type.
     */
public int enableApnType(java.lang.String type) throws android.os.RemoteException;
/**
     * Disable a specific APN type.
     */
public int disableApnType(java.lang.String type) throws android.os.RemoteException;
/**
     * Allow mobile data connections.
     */
public boolean enableDataConnectivity() throws android.os.RemoteException;
/**
     * Disallow mobile data connections.
     */
public boolean disableDataConnectivity() throws android.os.RemoteException;
/**
     * Report whether data connectivity is possible.
     */
public boolean isDataConnectivityPossible() throws android.os.RemoteException;
public android.os.Bundle getCellLocation() throws android.os.RemoteException;
/**
     * Returns the neighboring cell information of the device.
     *///List<NeighboringCellInfo> getNeighboringCellInfo();

public int getCallState() throws android.os.RemoteException;
public int getDataActivity() throws android.os.RemoteException;
public int getDataState() throws android.os.RemoteException;
/**
     * Returns the current active phone type as integer.
     * Returns TelephonyManager.PHONE_TYPE_CDMA if RILConstants.CDMA_PHONE
     * and TelephonyManager.PHONE_TYPE_GSM if RILConstants.GSM_PHONE
     */
public int getActivePhoneType() throws android.os.RemoteException;
/**
     * Returns the CDMA ERI icon index to display
     */
public int getCdmaEriIconIndex() throws android.os.RemoteException;
/**
     * Returns the CDMA ERI icon mode,
     * 0 - ON
     * 1 - FLASHING
     */
public int getCdmaEriIconMode() throws android.os.RemoteException;
/**
     * Returns the CDMA ERI text,
     */
public java.lang.String getCdmaEriText() throws android.os.RemoteException;
/**
     * Returns true if CDMA provisioning needs to run.
     */
public boolean getCdmaNeedsProvisioning() throws android.os.RemoteException;
/**
      * Returns the unread count of voicemails
      */
public int getVoiceMessageCount() throws android.os.RemoteException;
/**
      * Returns the network type
      */
public int getNetworkType() throws android.os.RemoteException;
/**
     * Return true if an ICC card is present
     */
public boolean hasIccCard() throws android.os.RemoteException;
//[ TelephonyFeature.CONFIG_FAKECALL_FEATURE

public void initiateFakecall() throws android.os.RemoteException;
//]
/**
     * Returns the Value of PIN remain retry number.
     */
public int getSimPinRetry() throws android.os.RemoteException;
/**
     * Returns the Value of PUK remain retry number.
     */
public int getSimPukRetry() throws android.os.RemoteException;
/**
     * Switch Holding and Active
     */
public void switchHoldingAndActive() throws android.os.RemoteException;
/**
     * Make call mute or unmute
     */
public void setMute(boolean flag) throws android.os.RemoteException;
/**
     * Return mute state
     */
public boolean getMute() throws android.os.RemoteException;
/**
     * Make speaker call on or off
     */
public void turnOnSpeaker(boolean flag) throws android.os.RemoteException;
/**
     * Return Active calls count.
     */
public int getActiveCallsCount() throws android.os.RemoteException;
/**
     * Return Hold calls count.
     */
public int getHoldCallsCount() throws android.os.RemoteException;
/**
     * Return Caller name
     * First active call has priority
     */
public java.lang.String getCallerName() throws android.os.RemoteException;
/**
     * Return Call time
     * First active call has priority
     */
public long getCallTime() throws android.os.RemoteException;
/**
     * Return Call base time
     * First active call has priority
     */
public long getCallBaseTime() throws android.os.RemoteException;
/**
     * Return TRUE, if current call is video call
     * First active call has priority
     */
public boolean isVideoCall() throws android.os.RemoteException;
// [TelephonyFeature.CONFIG_MULTIPLE_PDP_FEATURE

public boolean getDataStatebyApnType(java.lang.String ApnType) throws android.os.RemoteException;
}
