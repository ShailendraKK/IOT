using System.Data;
using System.Text;
using System.Configuration;
using System.Data.OleDb;
using System;
public partial class _Default : System.Web.UI.Page
{
    protected void Page_Load(object sender, EventArgs e)
    {

        OleDbConnection con = new OleDbConnection("Provider=Microsoft.Jet.OLEDB.4.0;Data Source=G:\\project\\FaceRecProOV\\Attendence.mdb");
        con.Open();
        OleDbDataAdapter da = new OleDbDataAdapter("select * from Student", con);
        DataTable Dt = new DataTable();
        da.Fill(Dt);
        GridView1.DataSource = Dt;
        GridView1.DataBind();
        con.Close();
    }

   
} 