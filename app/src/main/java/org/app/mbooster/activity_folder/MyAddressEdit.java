package org.app.mbooster.activity_folder;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.app.mbooster.Dialog.DialogFragmentUniversal;
import org.app.mbooster.Font.TypefaceUtil;
import org.app.mbooster.Helper.LogHelper;
import org.app.mbooster.R;
import org.app.mbooster.fragment_folder.EditAddress;
import org.app.mbooster.model_folder.Address;
import org.app.mbooster.model_folder.ButtonCountdown;
import org.app.mbooster.model_folder.NetworkStateReceiver;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.urlLink;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.view.View.GONE;

public class MyAddressEdit extends AppCompatActivity implements DialogFragmentUniversal.onSubmitListener, NetworkStateReceiver.NetworkStateReceiverListener {
    private LinearLayout shippingaddress, billingaddress;

//    private TextView sname, bname, saddress, baddress, scontect, bcontact, billedit, shipedit, toolbar_title;

    private TextView add_address;
    private Toolbar toolbar;

    private RadioButton rbtn1;
    private RadioButton rbtn2;

    private ListView shippinglist, billinglist;
    private ShipAddressAdapter shipadapter;
    private BillAddressAdapter billadapter;

    private ArrayList<Address> addressArrayList = new ArrayList<>();

    private RelativeLayout noshipbill;
    private TextView tvnoshipbill;

    private NetworkStateReceiver networkStateReceiver;
    static Activity activity;
    Context context;
    private int network = 0;
    private AlertDialog alertDialog, dialog;

    private ArrayList<ButtonCountdown> countdownarray = new ArrayList<>();
    private Gson gsonBuilder;
    private SwipeRefreshLayout refresh;

    private LinearLayout lyshipaddress_default, lybilladdress_default;
    private TextView tvshipaddress_default, tvbilladdress_default;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address_edit);

        context = activity = this;

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Gotham Rounded/GothamRnd-Book.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAddress();
            }
        });

        add_address = (TextView) findViewById(R.id.add_address);
        gsonBuilder = new GsonBuilder().create();

        noshipbill = (RelativeLayout) findViewById(R.id.noshipbill);
        tvnoshipbill = (TextView) findViewById(R.id.tvnoshipbill);

//        sname = (TextView) findViewById(R.id.shipname);
//        bname = (TextView) findViewById(R.id.billname);
//        saddress = (TextView) findViewById(R.id.shipaddress);
//        baddress = (TextView) findViewById(R.id.billaddress);
//        scontect = (TextView) findViewById(R.id.shipph);
//        bcontact = (TextView) findViewById(R.id.billph);
//        billedit = (TextView) findViewById(R.id.billedit);
//        shipedit = (TextView) findViewById(R.id.shipedit);

        lyshipaddress_default = (LinearLayout) findViewById(R.id.lyshipaddress_default);
        lybilladdress_default = (LinearLayout) findViewById(R.id.lybilladdress_default);
        tvshipaddress_default = (TextView) findViewById(R.id.tvshipaddress_default);
        tvbilladdress_default = (TextView) findViewById(R.id.tvbilladdress_default);

        rbtn1 = (RadioButton) findViewById(R.id.rbtn1);
        rbtn2 = (RadioButton) findViewById(R.id.rbtn2);
        shippingaddress = (LinearLayout) findViewById(R.id.shippingaddress);
        billingaddress = (LinearLayout) findViewById(R.id.billingaddress);

        shippinglist = (ListView) findViewById(R.id.ship_listview);
        billinglist = (ListView) findViewById(R.id.bill_listview);

        shipadapter = new ShipAddressAdapter(this, addressArrayList);
        billadapter = new BillAddressAdapter(this, addressArrayList);
        shippinglist.setAdapter(shipadapter);
        billinglist.setAdapter(billadapter);

        add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyAddressEdit.this, EditAddress.class);
                intent.putExtra("address_id", "0");
                startActivityForResult(intent, 1);
            }
        });


//        shippinglist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                //final ArrayList<ButtonCountdown> countdownarray = new ArrayList<>();
//                String JSONString = SavePreferences.getJSONCoundown(MyAddressEdit.this);
//
//                if (!JSONString.equals("0")) {
//                    try {
//                        countdownarray = gsonBuilder.fromJson(JSONString, new TypeToken<ArrayList<ButtonCountdown>>() {
//                        }.getType());
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                final Address item = addressArrayList.get(i);
//                final String id = addressArrayList.get(i).id;
//
//                final AlertDialog.Builder builderSingle = new AlertDialog.Builder(MyAddressEdit.this, R.style.AlertDialogTheme);
//                LayoutInflater inflater = MyAddressEdit.this.getLayoutInflater();
//                View dialogView = inflater.inflate(R.layout.alert_address_option, null);
//                builderSingle.setView(dialogView);
//
//                LinearLayout setdefaultlayout = (LinearLayout) dialogView.findViewById(R.id.setdefaultlayout);
//                LinearLayout verifylayout = (LinearLayout) dialogView.findViewById(R.id.verifylayout);
//                Button btnEdit = (Button) dialogView.findViewById(R.id.btnEdit);
//                Button btnsetshipping = (Button) dialogView.findViewById(R.id.btnsetshipping);
//                Button btnsetbilling = (Button) dialogView.findViewById(R.id.btnsetbilling);
//                Button btndeleteaddr = (Button) dialogView.findViewById(R.id.btndeleteaddr);
//                Button btnverify = (Button) dialogView.findViewById(R.id.btnverify);
//                TextView cancel = (TextView) dialogView.findViewById(R.id.cancel);
//                final Button btnsendverify = (Button) dialogView.findViewById(R.id.btnsendverify);
//
//
//                if (item.status.equals("2")) {
//                    setdefaultlayout.setVisibility(GONE);
//                    verifylayout.setVisibility(View.VISIBLE);
//                } else if (item.status.equals("1")) {
//                    setdefaultlayout.setVisibility(View.VISIBLE);
//                    verifylayout.setVisibility(GONE);
//                }
//
//                if (item.addr1.equals("") || item.icno.equals("") || item.name.equals("") || item.email.equals("") || item.state.equals("") || item.city.equals("") || item.postcode.equals("")) {
//                    setdefaultlayout.setVisibility(GONE);
//                    verifylayout.setVisibility(GONE);
//                    Toast.makeText(MyAddressEdit.this, R.string.pleasecompleteaddress, Toast.LENGTH_SHORT).show();
//                }
//                if (addressArrayList.size() == 1 || item.shipping.equals("1") || item.billing.equals("1")) {
//                    btndeleteaddr.setVisibility(GONE);
//                } else {
//                    btndeleteaddr.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            String arg = getString(R.string.are_you_sure_delete_address);
//                            String arg2 = getString(R.string.delete_address);
//                            String arg3 = getString(R.string.yes);
//                            String arg4 = getString(R.string.cancel);
//
//                            final Dialog dialog2 = new Dialog(MyAddressEdit.this, R.style.AlertDialogTheme);
//                            TypefaceUtil.overrideFont(MyAddressEdit.this, "SERIF", "fonts/gotham_book.otf");
//                            dialog2.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//                            dialog2.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                            dialog2.setContentView(R.layout.fragment_dialog_universal);
//                            dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                            dialog2.show();
//
//                            TextView content = (TextView) dialog2.findViewById(R.id.content);
//                            TextView title = (TextView) dialog2.findViewById(R.id.title);
//                            title.setText(arg2);
//                            Button btnConfirm = (Button) dialog2.findViewById(R.id.btnConfirm);
//                            Button btnCancel = (Button) dialog2.findViewById(R.id.btnCancel);
//
//                            btnConfirm.setText(arg3);
//                            btnCancel.setText(arg4);
//
//
//                            content.setText(arg);
//                            btnConfirm.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    dialog2.dismiss();
//                                    dialog.dismiss();
//                                    DeleteAddress(item.id);
//                                }
//                            });
//
//
//                            btnCancel.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    dialog2.dismiss();
//
//                                }
//                            });
//                        }
//                    });
//                }
//
//                btnEdit.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(context, EditAddress.class);
//                        intent.putExtra("address_id", item.id);
//                        startActivityForResult(intent, 1);
//                        dialog.dismiss();
//                    }
//                });
//
//                btnsetshipping.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        SetShipBill(id, "0");
//                        dialog.dismiss();
//                    }
//                });
//
//                btnsetbilling.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        SetShipBill(id, "1");
//                        dialog.dismiss();
//                    }
//                });
//
//
//                btnverify.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(context, VerifyAddress.class);
//                        intent.putExtra("address_id", item.id);
//                        startActivityForResult(intent, 1);
//                        dialog.dismiss();
//                    }
//                });
//
//
//                cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                    }
//                });
//
//                dialog = builderSingle.create();
//                dialog.show();
//            }
//        });

        rbtn1.setChecked(true);
        shippingaddress.setVisibility(View.VISIBLE);
        lyshipaddress_default.setVisibility(View.VISIBLE);
        billingaddress.setVisibility(View.INVISIBLE);
        lybilladdress_default.setVisibility(View.INVISIBLE);
        rbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shippingaddress.setVisibility(View.VISIBLE);
                lyshipaddress_default.setVisibility(View.VISIBLE);
                billingaddress.setVisibility(View.INVISIBLE);
                lybilladdress_default.setVisibility(View.INVISIBLE);
            }
        });

        rbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shippingaddress.setVisibility(View.INVISIBLE);
                lyshipaddress_default.setVisibility(View.INVISIBLE);
                billingaddress.setVisibility(View.VISIBLE);
                lybilladdress_default.setVisibility(View.VISIBLE);
            }
        });

//        shipedit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivityForResult(new Intent(MyAddressEdit.this, EditShipping.class), 1);
//            }
//        });
//
//        billedit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivityForResult(new Intent(MyAddressEdit.this, EditBilling.class), 1);
//            }
//        });


        ToobarSetting();
        getAddress();
    }

    @Override
    public void setOnSubmitListener(String arg) {
        Log.d("arg", arg.toString());
        if (arg.equals(getString(R.string.take_photo))) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            getAddress();
        }
    }

    private void ToobarSetting() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        //        final Drawable back_arrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        final Drawable back_arrow = VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back_black_24dp, null);

        back_arrow.setColorFilter(getResources().getColor(R.color.colorbutton), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    //get_address.php?user_id=
    private void getAddress() {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                addressArrayList.clear();
                if (!refresh.isRefreshing()) {
                    refresh.setRefreshing(true);
                }
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                refresh.setRefreshing(false);
                try {
                    if (jsonObject.getString("success").equals("1")) {
//                        sname.setText(jsonObject.getString("user_shipname"));
//                        saddress.setText(jsonObject.getString("user_shipaddress"));
//                        scontect.setText(jsonObject.getString("user_shipcontact"));
//                        bname.setText(jsonObject.getString("user_billname"));
//                        baddress.setText(jsonObject.getString("user_billaddress"));
//                        bcontact.setText(jsonObject.getString("user_billcontact"));

                        LogHelper.debug("[getAddress] jsonObject " + jsonObject.toString());

                        JSONArray addressarray = jsonObject.getJSONArray("address_array");

                        for (int i = 0; i < addressarray.length(); i++) {
                            JSONObject json = addressarray.getJSONObject(i);
                            addressArrayList.add(new Address(json.getString("address_id"),
                                    json.getString("name"),
                                    json.getString("contact"),
                                    json.getString("email"),
                                    json.getString("ic_no"),
                                    json.getString("addr1"),
                                    json.getString("addr2"),
                                    json.getString("postcode"),
                                    json.getString("city"),
                                    json.getString("state"),
                                    json.getString("country"),
                                    json.getString("shipping"),
                                    json.getString("billing"),
                                    json.getString("status")
                            ));

                            if (json.getString("shipping").equals("1")) {
                                String addrstr = json.getString("name") + "\n" + json.getString("contact") + "\n" + json.getString("email") + "\n" + json.getString("ic_no") + "\n\n" + json.getString("addr1") + ",\n" +
                                        json.getString("city") + ", " + json.getString("postcode") + ", " + json.getString("state") + ", " + json.getString("country");
                                tvshipaddress_default.setText(addrstr);
                            }

                            //No require check MMSpot anymore
                            if (json.getString("billing").equals("1")){// && SavePreferences.getUserType(MyAddressEdit.this).equals("0")) {
//                                String addrstr = SavePreferences.getMMSPOTNAME(MyAddressEdit.this) + "\n" + SavePreferences.getMMSPOTCONTACT(MyAddressEdit.this) + "\n" + SavePreferences.getMMSPOTEMAIL(MyAddressEdit.this) + "\n\n" + json.getString("addr1") + ",\n" +
//                                        json.getString("city") + ", " + json.getString("postcode") + ", " + json.getString("state") + ", " + json.getString("country");
//                                tvbilladdress_default.setText(addrstr);
//                            }
//                            else {
                                String addrstr = json.getString("name") + "\n" + json.getString("contact") + "\n" + json.getString("email") + "\n" + json.getString("ic_no") + "\n\n" + json.getString("addr1") + ",\n" +
                                        json.getString("city") + ", " + json.getString("postcode") + ", " + json.getString("state") + ", " + json.getString("country");
                                tvbilladdress_default.setText(addrstr);
                            }
                        }
                        shipadapter.notifyDataSetChanged();
                        billadapter.notifyDataSetChanged();

                        if (jsonObject.getString("default_shipping").equals("0") && jsonObject.getString("default_billing").equals("0")) {
                            noshipbill.setVisibility(View.VISIBLE);
                            tvnoshipbill.setText(R.string.no_ship_bill_default);
                        } else if (jsonObject.getString("default_shipping").equals("0")) {
                            noshipbill.setVisibility(View.VISIBLE);
                            tvnoshipbill.setText(R.string.no_ship_default);
                        } else if (jsonObject.getString("default_billing").equals("0")) {
                            noshipbill.setVisibility(View.VISIBLE);
                            tvnoshipbill.setText(R.string.no_bill_default);
                        } else {
                            noshipbill.setVisibility(GONE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().getAddress(SavePreferences.getUserID(MyAddressEdit.this));
            }
        }
        new getinfo().execute();
    }

    private void SetShipBill(final String address_id, final String shipbill) {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(MyAddressEdit.this)
                        .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                        .themeColor(getResources().getColor(R.color.colorbutton))
                        .fadeColor(Color.GRAY).build();

                if (!flowerDialog.isShowing()) {
                    flowerDialog.show();
                }
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);

                try {
                    if (jsonObject.getString("success").equals("1")) {
                        getAddress();
                    }
                    Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (flowerDialog.isShowing()) {
                    flowerDialog.dismiss();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().setShipBillDefault(SavePreferences.getUserID(MyAddressEdit.this), address_id, shipbill, SavePreferences.getApplanguage(MyAddressEdit.this));
            }
        }

        new getinfo().execute();
    }

    private void SendEmailVerifyAddr(final String address_id) {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(MyAddressEdit.this)
                        .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                        .themeColor(getResources().getColor(R.color.colorbutton))
                        .fadeColor(Color.GRAY).build();

                if (!flowerDialog.isShowing()) {
                    flowerDialog.show();
                }
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                if (flowerDialog.isShowing()) {
                    flowerDialog.dismiss();
                }
                try {
                    if (jsonObject.getString("success").equals("1")) {
                        shipadapter.notifyDataSetChanged();
                    }
                    Toast.makeText(MyAddressEdit.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().SendEmailVerifyAddr(SavePreferences.getUserID(MyAddressEdit.this), address_id, SavePreferences.getApplanguage(MyAddressEdit.this));
            }
        }
        new getinfo().execute();
    }

    private void DeleteAddress(final String address_id) {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(MyAddressEdit.this)
                        .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                        .themeColor(getResources().getColor(R.color.colorbutton))
                        .fadeColor(Color.GRAY).build();

                if (!flowerDialog.isShowing()) {
                    flowerDialog.show();
                }
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                if (flowerDialog.isShowing()) {
                    flowerDialog.dismiss();
                }
                try {
                    if (jsonObject.getString("success").equals("1")) {
                        getAddress();
                    }
                    Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().deleteAddress(SavePreferences.getUserID(MyAddressEdit.this), address_id, SavePreferences.getApplanguage(MyAddressEdit.this));
            }
        }
        new getinfo().execute();
    }

    class ShipAddressAdapter extends BaseAdapter {
        Context context;
        ArrayList<Address> items;
        LayoutInflater layoutInflater;

        public ShipAddressAdapter(Context context, ArrayList<Address> items) {
            this.context = context;
            this.items = items;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = layoutInflater.inflate(R.layout.address_item, null);

            TextView address = (TextView) convertView.findViewById(R.id.tvaddress);
            TextView status = (TextView) convertView.findViewById(R.id.status);
            SwitchCompat default_switch = (SwitchCompat) convertView.findViewById(R.id.default_switch);
            final RelativeLayout rl_resend = (RelativeLayout) convertView.findViewById(R.id.rl_resend);
            final TextView tv_resend = (TextView) convertView.findViewById(R.id.tv_resend);
            final RelativeLayout rldefault = (RelativeLayout) convertView.findViewById(R.id.rldefault);
            final TextView tvcountdown = (TextView) convertView.findViewById(R.id.coundown);
            RelativeLayout rl_editdelete = (RelativeLayout) convertView.findViewById(R.id.rl_editdelete);
            ImageView edit_address = (ImageView) convertView.findViewById(R.id.edit_address);
            ImageView delete_address = (ImageView) convertView.findViewById(R.id.delete_address);

            final Address item = (Address) getItem(position);
            String addrstr = item.name + "\n" + item.contact + "\n" + item.email + "\n" + item.icno + "\n\n" + item.addr1 + ",\n" +
                    item.city + ", " + item.postcode + ", " + item.state + ", " + item.country + "\n";
            address.setText(addrstr);

            if (item.status.equals("1")) {
                status.setTextColor(Color.parseColor("#39b54a"));
                status.setText(context.getString(R.string.verified));
                rl_resend.setVisibility(GONE);


            } else if (item.status.equals("2")) {
                status.setTextColor(Color.parseColor("#fb9301"));
                status.setText(context.getString(R.string.pending));
                rldefault.setVisibility(GONE);
            }

            //default_switch.setClickable(false);
            if (item.shipping.equals("1")) {
                default_switch.setChecked(true);
                default_switch.setClickable(false);
                delete_address.setVisibility(View.GONE);

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) edit_address.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                edit_address.setLayoutParams(params);

            } else {
                default_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        //if (isChecked) {
                        SetShipBill(item.id, "0");
//                        }
                    }
                });
            }

            edit_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String arg = getString(R.string.edit_address);
                    String arg2 = getString(R.string.edit_address);
                    String arg3 = getString(R.string.yes);
                    String arg4 = getString(R.string.cancel);

                    final Dialog dialog2 = new Dialog(MyAddressEdit.this, R.style.AlertDialogTheme);
                    TypefaceUtil.overrideFont(MyAddressEdit.this, "SERIF", "fonts/gotham_book.otf");
                    dialog2.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    dialog2.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    dialog2.setContentView(R.layout.fragment_dialog_universal);
                    dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog2.show();

                    TextView content = (TextView) dialog2.findViewById(R.id.content);
                    TextView title = (TextView) dialog2.findViewById(R.id.title);
                    title.setText(arg2);
                    Button btnConfirm = (Button) dialog2.findViewById(R.id.btnConfirm);
                    Button btnCancel = (Button) dialog2.findViewById(R.id.btnCancel);

                    btnConfirm.setText(arg3);
                    btnCancel.setText(arg4);

                    content.setText(arg);
                    btnConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                            Intent intent = new Intent(MyAddressEdit.this, EditAddress.class);
                            intent.putExtra("address_id", item.id);
                            startActivityForResult(intent, 1);
                        }
                    });

                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                        }
                    });
                }
            });


            delete_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String arg = getString(R.string.this_action_cannot_be_undone);
                    String arg2 = getString(R.string.are_you_sure_delete_address);
                    String arg3 = getString(R.string.yes);
                    String arg4 = getString(R.string.cancel);

                    final Dialog dialog2 = new Dialog(MyAddressEdit.this, R.style.AlertDialogTheme);
                    TypefaceUtil.overrideFont(MyAddressEdit.this, "SERIF", "fonts/gotham_book.otf");
                    dialog2.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    dialog2.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    dialog2.setContentView(R.layout.fragment_dialog_universal);
                    dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog2.show();

                    TextView content = (TextView) dialog2.findViewById(R.id.content);
                    TextView title = (TextView) dialog2.findViewById(R.id.title);
                    title.setText(arg2);
                    Button btnConfirm = (Button) dialog2.findViewById(R.id.btnConfirm);
                    Button btnCancel = (Button) dialog2.findViewById(R.id.btnCancel);

                    btnConfirm.setText(arg3);
                    btnCancel.setText(arg4);

                    content.setText(arg);
                    btnConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                            DeleteAddress(item.id);
                        }
                    });

                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                        }
                    });
                }
            });
            if (item.status.equals("2")) {
                rl_resend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SendEmailVerifyAddr(item.id);
                        rl_resend.setEnabled(false);
                        int tt = 0;
                        for (int j = 0; j < countdownarray.size(); j++) {
                            final ButtonCountdown countdown = countdownarray.get(j);
                            long now = System.currentTimeMillis();
                            long finishtime = now + 60000;
                            if (countdown.id.equals(item.id)) {
                                tt = 1;
                                countdown.update_countdowntime(finishtime);

                                break;
                            } else {
                                tt = 0;
                            }

                        }
                        long now = System.currentTimeMillis();
                        long finishtime = now + 60000;
                        if (tt == 0) {
                            ButtonCountdown countdown1 = new ButtonCountdown(item.id, finishtime);
                            countdownarray.add(countdown1);
                        }
                        String JSONFromArrayList = gsonBuilder.toJson(countdownarray);
                        SavePreferences.setJSONCoundown(MyAddressEdit.this, JSONFromArrayList);

                    }
                });


                String JSONString = SavePreferences.getJSONCoundown(MyAddressEdit.this);

                if (!JSONString.equals("0")) {
                    try {
                        countdownarray = gsonBuilder.fromJson(JSONString, new TypeToken<ArrayList<ButtonCountdown>>() {
                        }.getType());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                rl_resend.setEnabled(false);
                rl_resend.setBackground(context.getResources().getDrawable(R.drawable.customlayout_borderred3));
                tv_resend.setTextColor(context.getResources().getColor(R.color.grey));
                int tt = 0;
                for (int j = 0; j < countdownarray.size(); j++) {
                    final ButtonCountdown countdown = countdownarray.get(j);

                    if (countdown.id.equals(item.id)) {
                        long now = System.currentTimeMillis();
                        long endtime = countdown.coundowntime;
                        long timeleft = endtime - now;
                        if (timeleft > 0) {

                            new CountDownTimer(timeleft, 1000) {
                                public void onTick(long millisUntilFinished) {
                                    long now = System.currentTimeMillis();
                                    tvcountdown.setText("" + String.format("%d Sec", TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)));
                                    countdown.update_countdowntime(now + millisUntilFinished);
                                }

                                @Override
                                public void onFinish() {
                                    tvcountdown.setText("");
                                    rl_resend.setEnabled(true);
                                }
                            }.start();
                            tt = 1;
                        } else {
                            tt = 0;
                        }

                        break;
                    }
                }

                if (tt == 0) {
                    rl_resend.setEnabled(true);
                    rl_resend.setBackground(context.getResources().getDrawable(R.drawable.customlayout_borderred2));
                    tv_resend.setTextColor(Color.parseColor("#fb9301"));
                }
            }

            return convertView;
        }
    }

    class BillAddressAdapter extends BaseAdapter {
        Context context;
        ArrayList<Address> items;
        LayoutInflater layoutInflater;

        public BillAddressAdapter(Context context, ArrayList<Address> items) {
            this.context = context;
            this.items = items;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = layoutInflater.inflate(R.layout.address_item, null);

            TextView address = (TextView) convertView.findViewById(R.id.tvaddress);
            TextView status = (TextView) convertView.findViewById(R.id.status);
            TextView tvmakedefault = (TextView) convertView.findViewById(R.id.tvmakedefault);
            SwitchCompat default_switch = (SwitchCompat) convertView.findViewById(R.id.default_switch);
            final RelativeLayout rl_resend = (RelativeLayout) convertView.findViewById(R.id.rl_resend);
            final TextView tv_resend = (TextView) convertView.findViewById(R.id.tv_resend);
            final RelativeLayout rldefault = (RelativeLayout) convertView.findViewById(R.id.rldefault);
            final TextView tvcountdown = (TextView) convertView.findViewById(R.id.coundown);
            RelativeLayout rl_editdelete = (RelativeLayout) convertView.findViewById(R.id.rl_editdelete);
            ImageView edit_address = (ImageView) convertView.findViewById(R.id.edit_address);
            ImageView delete_address = (ImageView) convertView.findViewById(R.id.delete_address);

            tvmakedefault.setText(R.string.make_default_billing_address);

            final Address item = (Address) getItem(position);

            String addrstr = item.name + "\n" + item.contact + "\n" + item.email + "\n" + item.icno + "\n\n" + item.addr1 + ",\n" +
                    item.city + ", " + item.postcode + ", " + item.state + ", " + item.country + "\n";
            if (SavePreferences.getUserType(context).equals("0")) {
                addrstr = item.addr1 + ",\n" + item.city + ", " + item.postcode + ", " + item.state + ", " + item.country + "\n";
            }
            address.setText(addrstr);

            if (item.status.equals("1")) {
                status.setTextColor(Color.parseColor("#39b54a"));
                status.setText(context.getString(R.string.verified));
                rl_resend.setVisibility(GONE);


            } else if (item.status.equals("2")) {
                status.setTextColor(Color.parseColor("#fb9301"));
                status.setText(context.getString(R.string.pending));
                rldefault.setVisibility(GONE);
            }

            //default_switch.setClickable(false);
            if (item.billing.equals("1")) {
                default_switch.setChecked(true);
                default_switch.setClickable(false);
                delete_address.setVisibility(View.GONE);

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) edit_address.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                edit_address.setLayoutParams(params);

            } else {
                default_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        //if (isChecked) {
                        SetShipBill(item.id, "1");
//                        }
                    }
                });
            }

            edit_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String arg = getString(R.string.edit_address);
                    String arg2 = getString(R.string.edit_address);
                    String arg3 = getString(R.string.yes);
                    String arg4 = getString(R.string.cancel);

                    final Dialog dialog2 = new Dialog(MyAddressEdit.this, R.style.AlertDialogTheme);
                    TypefaceUtil.overrideFont(MyAddressEdit.this, "SERIF", "fonts/gotham_book.otf");
                    dialog2.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    dialog2.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    dialog2.setContentView(R.layout.fragment_dialog_universal);
                    dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog2.show();

                    TextView content = (TextView) dialog2.findViewById(R.id.content);
                    TextView title = (TextView) dialog2.findViewById(R.id.title);
                    title.setText(arg2);
                    Button btnConfirm = (Button) dialog2.findViewById(R.id.btnConfirm);
                    Button btnCancel = (Button) dialog2.findViewById(R.id.btnCancel);

                    btnConfirm.setText(arg3);
                    btnCancel.setText(arg4);

                    content.setText(arg);
                    btnConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                            Intent intent = new Intent(MyAddressEdit.this, EditAddress.class);
                            intent.putExtra("address_id", item.id);
                            startActivityForResult(intent, 1);
                        }
                    });

                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                        }
                    });
                }
            });

            delete_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String arg = getString(R.string.are_you_sure_delete_address);
                    String arg2 = getString(R.string.delete_address);
                    String arg3 = getString(R.string.yes);
                    String arg4 = getString(R.string.cancel);

                    final Dialog dialog2 = new Dialog(MyAddressEdit.this, R.style.AlertDialogTheme);
                    TypefaceUtil.overrideFont(MyAddressEdit.this, "SERIF", "fonts/gotham_book.otf");
                    dialog2.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    dialog2.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    dialog2.setContentView(R.layout.fragment_dialog_universal);
                    dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog2.show();

                    TextView content = (TextView) dialog2.findViewById(R.id.content);
                    TextView title = (TextView) dialog2.findViewById(R.id.title);
                    title.setText(arg2);
                    Button btnConfirm = (Button) dialog2.findViewById(R.id.btnConfirm);
                    Button btnCancel = (Button) dialog2.findViewById(R.id.btnCancel);

                    btnConfirm.setText(arg3);
                    btnCancel.setText(arg4);

                    content.setText(arg);
                    btnConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                            DeleteAddress(item.id);
                        }
                    });

                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                        }
                    });
                }
            });
            if (item.status.equals("2")) {
                rl_resend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SendEmailVerifyAddr(item.id);
                        rl_resend.setEnabled(false);
                        int tt = 0;
                        for (int j = 0; j < countdownarray.size(); j++) {
                            final ButtonCountdown countdown = countdownarray.get(j);
                            long now = System.currentTimeMillis();
                            long finishtime = now + 60000;
                            if (countdown.id.equals(item.id)) {
                                tt = 1;
                                countdown.update_countdowntime(finishtime);

                                break;
                            } else {
                                tt = 0;
                            }

                        }
                        long now = System.currentTimeMillis();
                        long finishtime = now + 60000;
                        if (tt == 0) {
                            ButtonCountdown countdown1 = new ButtonCountdown(item.id, finishtime);
                            countdownarray.add(countdown1);
                        }
                        String JSONFromArrayList = gsonBuilder.toJson(countdownarray);
                        SavePreferences.setJSONCoundown(MyAddressEdit.this, JSONFromArrayList);

                    }
                });


                String JSONString = SavePreferences.getJSONCoundown(MyAddressEdit.this);

                if (!JSONString.equals("0")) {
                    try {
                        countdownarray = gsonBuilder.fromJson(JSONString, new TypeToken<ArrayList<ButtonCountdown>>() {
                        }.getType());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                rl_resend.setEnabled(false);
                rl_resend.setBackground(context.getResources().getDrawable(R.drawable.customlayout_borderred3));
                tv_resend.setTextColor(context.getResources().getColor(R.color.grey));
                int tt = 0;
                for (int j = 0; j < countdownarray.size(); j++) {
                    final ButtonCountdown countdown = countdownarray.get(j);

                    if (countdown.id.equals(item.id)) {
                        long now = System.currentTimeMillis();
                        long endtime = countdown.coundowntime;
                        long timeleft = endtime - now;
                        if (timeleft > 0) {

                            new CountDownTimer(timeleft, 1000) {
                                public void onTick(long millisUntilFinished) {
                                    long now = System.currentTimeMillis();
                                    tvcountdown.setText("" + String.format("%d Sec", TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)));
                                    countdown.update_countdowntime(now + millisUntilFinished);
                                }

                                @Override
                                public void onFinish() {
                                    tvcountdown.setText("");
                                    rl_resend.setEnabled(true);
                                }
                            }.start();
                            tt = 1;
                        } else {
                            tt = 0;
                        }

                        break;
                    }
                }

                if (tt == 0) {
                    rl_resend.setEnabled(true);
                    rl_resend.setBackground(context.getResources().getDrawable(R.drawable.customlayout_borderred2));
                    tv_resend.setTextColor(Color.parseColor("#fb9301"));
                }
            }

            return convertView;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener((NetworkStateReceiver.NetworkStateReceiverListener) this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            this.unregisterReceiver(networkStateReceiver);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.registerReceiver(networkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

    }

    @Override
    public void networkAvailable() {
        network = 1;


    }

    @Override
    public void networkUnavailable() {

        network = 0;
        alertDialog = new AlertDialog.Builder(context, R.style.AlertDialogTheme)
                .setTitle(getString(R.string.no_network_notification))
                .setCancelable(false)

                .setPositiveButton(getString(R.string.try_again), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isOnline() == false) {
                            networkUnavailable();
                        } else {
                            dialog.dismiss();
                            getAddress();
                        }

                    }
                })
                .setNegativeButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((Activity) context).finish();
                    }
                })
                .create();

        if (network == 0) {

            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }
        }
    }


    public boolean isOnline() {


        try {

            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();


        } catch (Exception e) {

            return false;
        }

    }
}
