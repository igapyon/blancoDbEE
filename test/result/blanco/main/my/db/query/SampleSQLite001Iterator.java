/*
 * This code is generated by blanco Framework.
 */
package my.db.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import blanco.db.runtime.BlancoDbQuery;
import blanco.fw.BlancoGeneratedBy;

import my.db.exception.DeadlockException;
import my.db.exception.NoRowFoundException;
import my.db.exception.TimeoutException;
import my.db.exception.TooManyRowsFoundException;
import my.db.row.SampleSQLite001Row;
import my.db.util.BlancoDbUtil;

/**
 * [SampleSQLite001] Simple SQL sample. (QueryIterator)
 *
 * �����^SQL�������b�s���O���Ċe��A�N�Z�T��񋟂��܂��B<br>
 * �V���O������: �L�� (���҂��鏈��������1��)<br>
 * �X�N���[������: forward_only<br>
 */
@BlancoGeneratedBy(name = "blancoDb")
public class SampleSQLite001Iterator implements BlancoDbQuery {
    /**
     * ���̃N���X�������I�ɗ��p����f�[�^�x�[�X�ڑ��I�u�W�F�N�g�B
     *
     * �f�[�^�x�[�X�ڑ��I�u�W�F�N�g�̓R���X�g���N�^�̈����Ƃ��ĊO������^�����܂��B<br>
     * �g�����U�N�V�����̃R�~�b�g�⃍�[���o�b�N�́A���̃N���X�̓����ł͎��s���܂���B
     */
    protected Connection fConnection;

    /**
     * ���̃N���X�������I�ɗ��p����X�e�[�g�����g�I�u�W�F�N�g�B
     *
     * ���̃I�u�W�F�N�g�̓f�[�^�x�[�X�ڑ��I�u�W�F�N�g���琶������ē����I�ɗ��p����܂��B<br>
     * close���\�b�h�̌Ăяo�����ɁA���̃I�u�W�F�N�g��close�����s���܂��B
     */
    protected PreparedStatement fStatement;

    /**
     * ���̃N���X�������I�ɗ��p���錋�ʃZ�b�g�I�u�W�F�N�g�B
     *
     * ���̃I�u�W�F�N�g�̓f�[�^�x�[�X�X�e�[�g�����g�I�u�W�F�N�g���琶������ē����I�ɗ��p����܂��B<br>
     * close���\�b�h�̌Ăяo�����ɁA���̃I�u�W�F�N�g��close�����s���܂��B
     */
    protected ResultSet fResultSet;

    /**
     * SampleSQLite001Iterator�N���X�̃R���X�g���N�^�B
     *
     * �f�[�^�x�[�X�R�l�N�V�����I�u�W�F�N�g�������Ƃ��ăN�G���N���X���쐬���܂��B<br>
     * ���̃N���X�̗��p��́A�K�� close()���\�b�h���Ăяo���K�v������܂��B<br>
     *
     * @param conn �f�[�^�x�[�X�ڑ�
     */
    public SampleSQLite001Iterator(final Connection conn) {
        fConnection = conn;
    }

    /**
     * SampleSQLite001Iterator�N���X�̃R���X�g���N�^�B
     *
     * �f�[�^�x�[�X�R�l�N�V�����I�u�W�F�N�g��^�����ɃN�G���N���X���쐬���܂��B<br>
     */
    @Deprecated
    public SampleSQLite001Iterator() {
    }

    /**
     * SampleSQLite001Iterator�N���X�Ƀf�[�^�x�[�X�ڑ���ݒ�B
     *
     * @param conn �f�[�^�x�[�X�ڑ�
     */
    @Deprecated
    public void setConnection(final Connection conn) {
        fConnection = conn;
    }

    /**
     * SQL��`���ŗ^����ꂽSQL�����擾���܂��B
     *
     * SQL���̓p�����[�^�Ƃ��� #�L�[���[�h�ɂ��w�肪����ꍇ�ɂ́A�Y���ӏ��� ? �ɒu����������� SQL�����擾�ł��܂��B
     *
     * @return JDBC�h���C�o�ɗ^���Ď��s�\�ȏ�Ԃ�SQL���B
     */
    public String getQuery() {
        return "SELECT\n        *\n    FROM\n        TEST_BLANCODB\n    WHERE\n        col_id = ?";
    }

    /**
     * SQL��`������^����ꂽSQL�����������ăv���R���p�C�������{���܂��B
     *
     * �����I��Connection.prepareStatement���Ăяo���܂��B<br>
     *
     * @throws SQLException SQL��O�����������ꍇ�B
     */
    public void prepareStatement() throws SQLException {
        close();
        prepareStatement(getQuery());
    }

    /**
     * �^����ꂽSQL�����������ăv���R���p�C�������{(���ISQL)���܂��B
     *
     * ���̃��\�b�h�́A���I�ɓ��e���ω�����悤�� SQL �����s����K�v������ꍇ�ɂ̂ݗ��p���܂��B<br>
     * ���I SQL �𗘗p����K�v������ꍇ�ɂ́ASQL ��`���Łu���ISQL�v���u�g�p����v�ɕύX���Ă��������B�ύX��͊O�����痘�p�\�ɂȂ�܂��B<br>
     * �����I�� JDBC �h���C�o�� Connection.prepareStatement ���Ăяo���܂��B<br>
     *
     * @param query �v���R���p�C�������{��������SQL���B���ISQL�̏ꍇ�ɂ́A���̈����ɂ͉��H���ꂽ��̎��s�\��SQL����^���܂��B
     * @throws SQLException SQL��O�����������ꍇ�B
     */
    protected void prepareStatement(final String query) throws SQLException {
        close();
        fStatement = fConnection.prepareStatement(query);
    }

    /**
     * SQL���ɗ^����SQL���̓p�����[�^���Z�b�g���܂��B
     *
     * �����I�ɂ� PreparedStatement��SQL���̓p�����[�^���Z�b�g���܂��B
     *
     * @param col_id 'col_id'��̒l
     * @throws SQLException SQL��O�����������ꍇ�B
     */
    public void setInputParameter(final int col_id) throws SQLException {
        if (fStatement == null) {
            prepareStatement();
        }
        fStatement.setInt(1, col_id);
    }

    /**
     * �����^�N�G�������s���܂��B<br>
     *
     * @throws DeadlockException �f�[�^�x�[�X�f�b�h���b�N�����������ꍇ�B
     * @throws TimeoutException �f�[�^�x�[�X�^�C���A�E�g�����������ꍇ�B
     * @throws SQLException SQL��O�����������ꍇ�B
     */
    public void executeQuery() throws DeadlockException, TimeoutException, SQLException {
        if (fStatement == null) {
            // PreparedStatement�����擾�̏�ԂȂ̂ŁAPreparedStatement.executeQuery()���s�ɐ旧��prepareStatement()���\�b�h���Ăяo���Ď擾���܂��B
            prepareStatement();
        }
        if (fResultSet != null) {
            // �O��̌��ʃZ�b�g(ResultSet)���c���Ă���̂ŁA�������U�J�����܂��B
            fResultSet.close();
            fResultSet = null;
        }

        try {
            fResultSet = fStatement.executeQuery();
        } catch (SQLException ex) {
            throw BlancoDbUtil.convertToBlancoException(ex);
        }
    }

    /**
     * �J�[�\�������݂̈ʒu����1�s���ֈړ����܂��B
     * �V���O���������L���Ȃ̂ŃX�R�[�v��protected�Ƃ��܂��B<br>
     *
     * @return �V�������݂̍s���L���ȏꍇ��true�A����ȏ�̍s���Ȃ��ꍇ��false�B
     * @throws DeadlockException �f�[�^�x�[�X�f�b�h���b�N�����������ꍇ�B
     * @throws TimeoutException �f�[�^�x�[�X�^�C���A�E�g�����������ꍇ�B
     * @throws SQLException SQL��O�����������ꍇ�B
     */
    protected boolean next() throws DeadlockException, TimeoutException, SQLException {
        if (fResultSet == null) {
            executeQuery();
        }

        try {
            return fResultSet.next();
        } catch (SQLException ex) {
            throw BlancoDbUtil.convertToBlancoException(ex);
        }
    }

    /**
     * ���݂̍s�̃f�[�^���I�u�W�F�N�g�Ƃ��Ď擾���܂��B
     *
     * �V���O���������L���Ȃ̂ŃX�R�[�v��protected�Ƃ��܂��B<br>
     * ���̃��\�b�h�̑���� getSingleRow���\�b�h�𗘗p���Ă��������B<br>
     *
     * @return �s�I�u�W�F�N�g�B
     * @throws SQLException SQL��O�����������ꍇ�B
     */
    protected SampleSQLite001Row getRow() throws SQLException {
        SampleSQLite001Row result = new SampleSQLite001Row();
        result.setColId(fResultSet.getInt(1));
        result.setColText(fResultSet.getString(2));
        result.setColNumeric(new Double(fResultSet.getDouble(3)));
        if (fResultSet.wasNull()) {
            result.setColNumeric(null);
        }

        return result;
    }

    /**
     * �X�e�[�g�����g (java.sql.PreparedStatement) ���擾���܂��B
     * @deprecated ��{�I��Statement�͊O�����璼�ڗ��p����K�v�͂���܂���B
     *
     * @return �����I�ɗ��p����Ă��� java.sql.PreparedStatement�I�u�W�F�N�g
     */
    public PreparedStatement getStatement() {
        return fStatement;
    }

    /**
     * �����I�ɕێ�����Ă���ResultSet�I�u�W�F�N�g���擾���܂��B
     *
     * @deprecated ��{�I��ResultSet�͊O�����璼�ڗ��p����K�v�͂���܂���B
     *
     * @return ResultSet�I�u�W�F�N�g�B
     */
    public ResultSet getResultSet() {
        return fResultSet;
    }

    /**
     * ���݂̍s�̃f�[�^���I�u�W�F�N�g�Ƃ��Ď擾���܂��B
     *
     * SQL���̎��s���ʂ�1�s�ł��邱�Ƃ��m�F���܂��B���s���ʂ�1�s�ȊO�ł���ꍇ�ɂ͗�O�𔭐������܂��B<br>
     * �V���O���������L���ƂȂ��Ă���̂Ő�������܂��B<br>
     *
     * @return �s�I�u�W�F�N�g�B
     * @throws NoRowFoundException �f�[�^�x�[�X�̏����̌��ʁA1�s���f�[�^����������Ȃ������ꍇ�B
     * @throws TooManyRowsFoundException �f�[�^�x�[�X�̏����̌��ʁA1�s�𒴂���f�[�^����������Ă��܂����ꍇ�B
     * @throws SQLException SQL��O�����������ꍇ�B
     */
    public SampleSQLite001Row getSingleRow() throws NoRowFoundException, TooManyRowsFoundException, SQLException {
        if (next() == false) {
            throw new NoRowFoundException("�f�[�^�x�[�X�̏����̌��ʁA1�s���f�[�^����������܂���ł����B");
        }

        SampleSQLite001Row result = getRow();

        if (next()) {
            throw new TooManyRowsFoundException("�f�[�^�x�[�X�̏����̌��ʁA1�s�𒴂���f�[�^����������܂����B");
        }

        return result;
    }

    /**
     * ���̃N���X�̃N���[�Y�����������Ȃ��܂��B
     *
     * �����I�ɐ������Ă���JDBC���\�[�X�̃I�u�W�F�N�g�ɑ΂��� close()���\�b�h�̌Ăяo���������Ȃ��܂��B<br>
     * �N���X�̗��p���I�������A�K�����̃��\�b�h���Ăяo���悤�ɂ��܂��B
     *
     * @throws SQLException SQL��O�����������ꍇ�B
     */
    public void close() throws SQLException {
        try {
            if (fResultSet != null) {
                fResultSet.close();
                fResultSet = null;
            }
        } finally {
            if (fStatement != null) {
                fStatement.close();
                fStatement = null;
            }
        }
    }

    /**
     * finalize���\�b�h�B
     *
     * ���̃N���X�������I�ɐ��������I�u�W�F�N�g�̂Ȃ��ŁAclose()�Ăяo���Y��o�O�����݂��邩�ǂ����`�F�b�N���܂��B<br>
     *
     * @throws Throwable finalize�����̒��Ŕ���������O�B
     */
    protected void finalize() throws Throwable {
        super.finalize();
        if (fStatement != null) {
            final String message = "SampleSQLite001Iterator : close()���\�b�h�ɂ�郊�\�[�X�̊J�����s���Ă��܂���B";
            System.out.println(message);
        }
    }
}