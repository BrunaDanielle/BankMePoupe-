package com.example.contacorrenteadm.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contacorrenteadm.R;
import com.example.contacorrenteadm.base.BackButtonSupportFragment;
import com.example.contacorrenteadm.base.BaseFragment;
import com.example.contacorrenteadm.data.BanckServiceImpl;
import com.example.contacorrenteadm.interfaces_contract.TransferContract;
import com.example.contacorrenteadm.model.AuthenticationTransfer;
import com.example.contacorrenteadm.model.Client;
import com.example.contacorrenteadm.model.Login;
import com.example.contacorrenteadm.presenter.TransferPresenter;

import java.util.Locale;

import static android.content.DialogInterface.*;

public class TransferFragment extends BaseFragment implements TransferContract.ViewTransfer {

    private boolean consumingBackPress = true;
    private EditText valueToSend;
    private EditText emailUserTo;
    private TextView valueBalance;
    private TransferContract.UserActionTransfer userActionTransfer;
    private Object OnClickListener;
    private Button btnTransfer;
    private int idUser;

    public static BaseFragment newInstance() {
        return new TransferFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userActionTransfer = new TransferPresenter(new BanckServiceImpl(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_transfer, container, false);
        if (getArguments() != null) {
            idUser = getArguments().getInt("idUserSent");
        }
        valueToSend = root.findViewById(R.id.value_to_send);
        emailUserTo = root.findViewById(R.id.email_receiver);
        btnTransfer = root.findViewById(R.id.buttonTransfer);
        valueBalance = root.findViewById(R.id.value_available);

        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userActionTransfer.putEmailUserTo(emailUserTo.getText().toString());
            }
        });
        return root;
    }

    @Override
    protected String getTitle() {
        return "Transferência";
    }

    @Override
    public boolean onBackPressed() {
        if (consumingBackPress) {
            consumingBackPress = false;
            return true;
        }
        return false;
    }

    @Override
    public void showTransfer(Boolean authentication) {
        if (authentication) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder.setPositiveButton("Finalizar", (DialogInterface.OnClickListener) OnClickListener);
            alertDialogBuilder.setMessage("Transferência realizada com sucesso!");
            alertDialogBuilder.setTitle("Comprovante");
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder.setPositiveButton("Okay", (DialogInterface.OnClickListener) OnClickListener);
            alertDialogBuilder.setMessage("Email inválido!");
            alertDialogBuilder.setTitle("Erro");
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    @Override
    public void getIdUser(Client client) {
        userActionTransfer.doTransfer(idUser, client.id, Double.parseDouble(valueToSend.getText().toString()));
    }

    @Override
    public void onError() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setPositiveButton("Okay", (DialogInterface.OnClickListener) OnClickListener);
        alertDialogBuilder.setMessage("Email inválido!");
        alertDialogBuilder.setTitle("Erro");
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}